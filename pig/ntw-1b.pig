-- Load input data from local input directory
RUN load.pig;

-- Group by client IP
ip_client = GROUP dataset BY ip_c;
ip_server = GROUP dataset BY ip_s;

-- Merge the two datasets;
united = UNION ip_client, ip_server;

-- Generate the output data
output_data = FOREACH united GENERATE group, COUNT(dataset);

-- Order output data by ip
sorted_data = ORDER output_data BY group;

-- Store the output and start to execute the script
STORE sorted_data INTO 'output/ntw_1b';
