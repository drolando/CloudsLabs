data = LOAD './sample-input/AIRLINE/test.csv' USING PigStorage(',') AS 
--data = LOAD '/laboratory/input/airlines/2008.csv' USING PigStorage(',') AS
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

routes = GROUP data BY (scode < dcode ? (scode, dcode) : (dcode, scode));
traffic = FOREACH routes GENERATE group, COUNT(data) as flights;

traffic_ord = ORDER traffic BY flights DESC;
traffic_20 = LIMIT traffic_ord 20;

-- TOP 20 routes by flights
--STORE traffic_20 INTO './local-output/AIR_5_TOP20/';
DUMP traffic_20;

