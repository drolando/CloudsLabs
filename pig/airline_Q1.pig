--data = LOAD './sample-input/AIRLINE/test.csv' USING PigStorage(',') AS 
data = LOAD '/laboratory/input/airlines/2008.csv' USING PigStorage(',') AS
	(year: int, month: int, day: int, dow: int, 
	dtime: int, sdtime: int, arrtime: int, satime: int, 
	carrier: chararray, fn: int, tn: chararray, 
	etime: int, setime: int, airtime: int, 
	adelay: int, ddelay: int, 
	scode: chararray, dcode: chararray, dist: int, 
	tintime: int, touttime: int, 
	cancel: chararray, cancelcode: chararray, diverted: int, 
	cdelay: int, wdelay: int, ndelay: int, sdelay: int, latedelay: int);

airports_dirty = LOAD './sample-input/AIRLINE/airports.csv' USING PigStorage(',') AS 
	(code: chararray, airport: chararray, city: chararray);

airports = FOREACH airports_dirty GENERATE
    (chararray) REPLACE((chararray)$0, '\\"', '') AS code:chararray,
    (chararray) REPLACE((chararray)$1, '\\"', '') AS airport:chararray,
    (chararray) REPLACE((chararray)$2, '\\"', '') AS city:chararray;

-- TOTAL STATISTICS
groups_in = GROUP data by dcode;
inbound = FOREACH groups_in GENERATE group as code, COUNT(data) as flights;

groups_out = GROUP data by scode;
outbound = FOREACH groups_out GENERATE group as code, COUNT(data) as flights;

total_data = UNION inbound, outbound;
groups_total = GROUP total_data by code;
total = FOREACH groups_total GENERATE group AS code, SUM(total_data.flights) AS flights;

total_ord = ORDER total BY flights DESC;
total_20 = LIMIT total_ord 20;
total_20_c = JOIN total_20 BY code, airports BY code;
total_20_cities = FOREACH total_20_c GENERATE city, flights;

-- TOP 20 cities names by flights
STORE total_20_cities INTO './local-output/AIR_1_TOP20/';


-- DAILY STATISTICS
groups_in_d = GROUP data by (day, dcode);
inbound_d = FOREACH groups_in_d GENERATE group as code, COUNT(data) as flights;

groups_out_d = GROUP data by (day, scode);
outbound_d = FOREACH groups_out_d GENERATE group as code, COUNT(data) as flights;

total_data_d = UNION inbound_d, outbound_d;

group_total_d = GROUP total_data_d BY code.day;
group_total_top_d = FOREACH group_total_d {
    result = TOP(20, 1, total_data_d); 
    GENERATE FLATTEN(result);
}

daily_top_20 = JOIN group_total_top_d BY code.$1, airports BY code;
daily_top_20_c = FOREACH daily_top_20 GENERATE $0.$0, city, flights;

-- TOP 20 cities names by flights DAILY
STORE daily_top_20_c INTO './local-output/AIR_1_TOP20_DAILY/';

