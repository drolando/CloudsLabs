/*
  find the top 100 users per uploaded bytes.
*/

-- Load input data from local input directory
RUN load.pig;

-- group by client ip
connections = GROUP dataset BY ip_c;

-- compute the number of bytes sent by each client
upload = FOREACH connections GENERATE group as ip, SUM(dataset.unique_bytes_c) as uploaded;

-- group operation required by the FOREACH
tmp = GROUP upload ALL;

-- get the top 100 clients
topResults = FOREACH tmp {
    result = TOP(100, 1, upload);
    GENERATE FLATTEN(result);
}

-- Start executing the script and save the results
STORE topResults INTO 'output/ntw_4';
