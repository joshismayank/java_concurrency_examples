import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class WordCountWithFutures {

	private static final int BATCH_SIZE = 1000;
	private static final String INPUT_FILE = "input_file";
	private static final String KEYWORDS_FILE = "keywords_file";
	private static final String OUTPUT_FILE = "wordCountWithFutures.txt";

	private Map<String,String> extractArgs(String[] args) {
		Map<String,String> argsMap = new HashMap<>();
		for (String arg: args) {
			String[] currArgParts = arg.split("=");
			argsMap.put(currArgParts.length<1?"":currArgParts[0], currArgParts.length>1?currArgParts[1]:"");
		}
		if (!argsMap.containsKey(INPUT_FILE)||argsMap.get(INPUT_FILE).isEmpty()) {
			System.out.println("req format : java WordCount input_file=someFile keywords_file=anotherFile");
			throw new RuntimeException("no file provided for "+INPUT_FILE);
		}
		if (!argsMap.containsKey(KEYWORDS_FILE)||argsMap.get(KEYWORDS_FILE).isEmpty()) {
			System.out.println("req format : java WordCount input_file=someFile keywords_file=anotherFile");
			throw new RuntimeException("no file provided for "+KEYWORDS_FILE);
		}
		return argsMap;
	}

	private List<String> getLinesFromFile(String filename) {
		try {
			return Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);
		} catch (Exception ex) {
			throw new RuntimeException("could not read file "+filename+" : "+ex.toString());
		}
	}

	private void writeToFile(Map<String,Integer> wordCount) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE))) {
			wordCount.forEach((key, value) -> {
				try {
					writer.write(key+" - "+value);
					writer.newLine();
				} catch (Exception ex) {
					System.out.println("could not write key "+key+" : "+ex.toString());
				}
			});
		} catch(Exception ex) {
			System.out.println(ex);
			throw new RuntimeException("could not write word count in file "+OUTPUT_FILE+" : "+ex.toString());
		}
	}

	private List<List<String>> getBatches(List<String> lines) {
		List<List<String>> batches = new ArrayList<>();
		for (int i=0;i<lines.size();i+=BATCH_SIZE) {
			List<String> batch = lines.subList(i,Math.min(lines.size(),i+BATCH_SIZE));
			batches.add(batch);
		}
		return batches;
	}

	private Map<String,Integer> computeWordCountForOneBatch(List<String> lines, Set<String> keywords) {
		Map<String,Integer> wordCount = new HashMap<>();
		lines.forEach(line->{
			List<String> words = new ArrayList<>(Arrays.asList(line.split(" ")));
			words.forEach(word->{
				word = word.isEmpty()||Character.isLetterOrDigit(word.charAt(word.length()-1))?word:word.substring(0, word.length()-1);
				word = word.toLowerCase();
				if (keywords.contains(word)) {
					wordCount.put(word, wordCount.getOrDefault(word, 0)+1);
				}
			});
		});
		return wordCount;
	}

	private Map<String,Integer> computeWordCount(List<List<String>> batches, Set<String> keywords) {
		List<CompletableFuture<Map<String,Integer>>> futures = batches.stream().map(batch -> {
			return CompletableFuture.supplyAsync(() -> {
				return computeWordCountForOneBatch(batch,keywords);
			});
		}).collect(Collectors.toList());
		CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
		
		List<Map<String,Integer>> wordCountResults = allFutures.thenApply(f->
				futures.stream().map(CompletableFuture::join).collect(Collectors.toList())
				).join();
		
		Optional<Map<String, Integer>> wordCountCombinedOptional = wordCountResults.stream().reduce((firstMap, secondMap) -> {
			return Stream.concat(firstMap.entrySet().stream(), secondMap.entrySet().stream())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
							(countInFirstMap, countInSecondMap) 
							-> countInFirstMap + countInSecondMap));
		});
		
		return wordCountCombinedOptional.get();
	}

	private void processFiles(String[] args) {
		Map<String,String> argsMap = extractArgs(args);
		
		Set<String> keywords = new HashSet<>();
		getLinesFromFile(argsMap.get(KEYWORDS_FILE)).forEach(word->{
			keywords.add(word.toLowerCase());
		});
		List<String> inputLines = getLinesFromFile(argsMap.get(INPUT_FILE));
		List<List<String>> inputBatches = getBatches(inputLines);

		Map<String,Integer> wordCount = computeWordCount(inputBatches, keywords);
	
		writeToFile(wordCount);
	}

	public static void main(String[] args) {
		WordCountWithFutures wordCount = new WordCountWithFutures();
		wordCount.processFiles(args);
	}
}
