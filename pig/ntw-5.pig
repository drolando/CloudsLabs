/*
  For each IP in the top 100 list previously computed, find the number of bytes uploaded by the largest TCP connection and the percentage of the bytes uploaded by this connection over the total number of uploaded bytes.

*/

-- Load input data from local input directory
RUN load.pig;

-- group by client ip
connections = GROUP dataset BY ip_c;

-- compute the number of bytes sent by each client
upload = FOREACH connections GENERATE group as ip, SUM(dataset.unique_bytes_c) as uploaded, MAX(dataset.unique_bytes_c) as biggest;

-- group operation required by the FOREACH
tmp = GROUP upload ALL;

-- get the top 100 clients
topResults = FOREACH tmp {
    result = TOP(100, 1, upload);
    GENERATE FLATTEN(result);
}

total_group = GROUP dataset ALL;
total = FOREACH total_group GENERATE SUM(dataset.unique_bytes_c) as sum;

topRes_grouped = GROUP topResults BY ip;

DESCRIBE topRes_grouped;
output_data = FOREACH topRes_grouped GENERATE topResults.ip as ip, topResults.uploaded as total, topResults.biggest as largest_tcp, topRes_grouped.topResults.biggest/total.$0 as ratio;

-- Start executing the script and save the results
-- STORE output_data INTO 'output/ntw_5';
DUMP output_data;
