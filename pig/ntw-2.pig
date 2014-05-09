/*
  count the total number of TCP connection having $domain in the FQDN parameter
  INPUT:
    -- domain : string to be searched in the FQDN parameter
  To execute the script run:
    pig -x mapreduce -param domain='google' -f ntw-2.pig
*/

-- set default parallel to 20
set default_parallel 20;

-- Load input data from local input directory
RUN load.pig;

-- Filter the dataset
filtered = FILTER dataset BY (fqdn matches '.*$domain.*');

-- Group together data with the same fqdn
grouped = GROUP filtered ALL;

-- Count number of connection for fqdn
conn_count  = FOREACH grouped GENERATE '$domain', COUNT(filtered);

-- Start executing the script and store the result
STORE conn_count INTO 'output/ntw_2';
