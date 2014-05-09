-- Load input data from local input directory
RUN load.pig;

-- Group by client IP
ip_client = GROUP dataset BY ip_c;


-- Generate the output data
conn_per_client = FOREACH ip_client GENERATE group, COUNT(dataset);


-- Store the output (and start to execute the script)
STORE conn_per_client INTO 'output/ntw_1';
