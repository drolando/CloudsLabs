/*
  For each client IP, compute the sum of uploaded, downloaded and total (up+down) transmitted bytes.
*/

-- Load input data from local input directory
RUN load.pig;

-- group by client ip
connections = GROUP dataset BY ip_c;

-- evaluate the uploaded and downloaded data
up_down = FOREACH connections GENERATE $0 as ip, SUM($1.unique_bytes_c) as up, SUM($1.unique_bytes_s) as down;

-- Compute the total exchanged data
total = FOREACH up_down GENERATE ip, up, down, up + down as total;

-- Start executing the script and store the results
STORE total INTO 'output/ntw_3';
