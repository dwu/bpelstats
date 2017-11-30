Anonymization in BPELStats
==========================

BPELStats offers anonymization capabilities in order to exchange statistics between different parties (e.g. development projects and researchers). The anonymization faciliities are controlled by the -a and -p switches.

* -a will replace all file names with a SHA-512 hash of the UTF-8 encoded file name. If the Java Runtime does not support SHA-512 or UTF-8 a simple java.lang.String.hashCode() will be returned. You can see this because the hash is a small number. 

* -p will print the original file name followed by an underscore followed by the hash as produced with -a

-a is intended to be used to create statistics that can be shared between different parties. However, the party receiving ("partner") the statistics may have questions and will come back to the process owner. The process owner can use the -p switch to resolve the hash delivered by the partner.

-a will always override -p, i.e. if you specify both it will have the same effect as if you only specified -a.
