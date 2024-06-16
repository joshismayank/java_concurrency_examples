(needs improvement - is it correct? is it complete?)
- in case of multiple threads:
  - to ensure only one thread manipulates an object:
    - use synchronized
    - use locks if more control is needed (synchronized is implented through locks internally)
  - to signal other thread
    - use conditional variables of lock
    - use semaphore
   
- problems todo:
  - dining philosopher (n number of philosophers)
  - producer consumer (p producers, c consumers, s size of buffer)
  - reader writer (r reader, w writers)
  - barrier synchronization (n threads)
  - concurrent list
