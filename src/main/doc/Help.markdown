usage: bpelstats file.bpel... [options]
 -a                          Anonymize BPEL file names (overrides -p)
 -c                          Gather BPEL Complexity Metrics as used in
                             Juric & Hertis 2014 (excludes -e and -s)
 -d <delimiter>              Use a custom delimiter (default=,)
 -e                          Gather BPEL Extensions and
                             ExtensionActivities (excludes -c and -s)
 -f <FILE>                   Writes statistics into a file.
 -h                          Writes out the table header
    --help                   Prints this help
 -p                          Uses pseudo anonymization printing both
                             anonymized and non-anonymized file names
                             (overridden by -a)
    --reusedir <directory>   Directories for re-usable assets
                             (XSLT/XQuery). Only applicable for -s option
 -s                          Gathers sub-language statistics (excludes -c
                             and -e)
