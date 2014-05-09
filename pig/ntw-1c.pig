/*
  count the number of TCP connection per each client IP, at each of the following time granularities: hour, day, week, month, year...
  INPUT:
    -- group : time range: can be 'hour', 'day', 'week', 'month', 'year'
  To execute the script run:
    pig -x mapreduce -param group='week' -f ntw-1c.pig
*/

-- Load input data from local input directory
RUN load.pig;

-- Group by client IP
time_data = FOREACH dataset GENERATE ip_c,
	GetHour(ToDate((long) first_time_abs)) as hour,
	GetDay(ToDate((long) first_time_abs)) as day,
	GetWeek(ToDate((long) first_time_abs)) as week,
	GetMonth(ToDate((long) first_time_abs)) as month,
	GetYear(ToDate((long) first_time_abs)) as year;


-- Group the data by the desired time range
grouped_data = GROUP time_data BY (ip_c, $group);

-- Count the number of connection per client
connection_counts = FOREACH grouped_data GENERATE $0.ip_c, $0.$group, COUNT($1) as total;

-- Order output data by ip
sorted_data = ORDER connection_counts BY ip_c;

-- Store the output (and start to execute the script)
STORE sorted_data INTO 'output/ntw_1c';




