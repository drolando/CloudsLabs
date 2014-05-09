/*
  For each IP in the top 100 list previously computed, find the number of bytes uploaded by the largest TCP connection and the percentage of the bytes uploaded by this connection over the total number of uploaded bytes.

*/

-- Load input data from local input directory
RUN load.pig;

-- Group by client IP
connections = GROUP dataset BY ip_c;

-- compute the number of bytes sent by each client
upload = FOREACH connections GENERATE group as ip, SUM(dataset.unique_bytes_c) as up;

-- group operation required by the FOREACH
tmp = GROUP upload ALL;
total = FOREACH tmp GENERATE SUM(upload.up);

-- get the top 100 clients
top = FOREACH tmp { result = TOP(100, 1, upload); GENERATE FLATTEN(result); }

ordered = ORDER top by upload::up DESC;

output_data = FOREACH ordered GENERATE ip, ((double)up)/total.$0;

-- Start executing the script and save the results
STORE output_data INTO 'output/ntw_5';
