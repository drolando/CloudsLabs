/*
  For each IP in the top 100 list previously computed, find the number of bytes uploaded by the largest TCP connection and the percentage of the bytes uploaded by this connection over the total number of uploaded bytes.

*/

-- set default parallel to 20
set default_parallel 20;

-- Load input data from local input directory
RUN load.pig;

-- Group by client IP
connections = GROUP dataset BY ip_c;

-- compute the number of bytes sent by each client
upload = FOREACH connections GENERATE group as ip, SUM(dataset.unique_bytes_c) as up;

-- group operation required by the FOREACH
tmp = GROUP upload ALL;

-- get the top 100 clients
top = FOREACH tmp { result = TOP(100, 1, upload); GENERATE FLATTEN(result); }

biggest = FOREACH connections GENERATE group as ip, MAX(dataset.unique_bytes_c) as big;

joined = JOIN biggest BY ip, top by upload::ip;


output_data = FOREACH joined GENERATE upload::ip, ((double)big)/up;

-- Start executing the script and save the results
STORE output_data INTO 'output/ntw_5';

