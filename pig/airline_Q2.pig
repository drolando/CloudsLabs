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

carriers_dirty = LOAD './sample-input/AIRLINE/carriers.csv' USING PigStorage(',') AS
        (code: chararray, name: chararray);

carriers= FOREACH carriers_dirty GENERATE
    (chararray) REPLACE((chararray)$0, '\\"', '') AS code:chararray,
    (chararray) REPLACE((chararray)$1, '\\"', '') AS name:chararray;

carrier = FOREACH data GENERATE year, carrier AS code;
group_carriers = GROUP carrier BY (year,code);
count = FOREACH group_carriers GENERATE FLATTEN(group), LOG10(COUNT(carrier)) AS popularity;
count_name = JOIN count BY group::code, carriers BY code;
out = FOREACH count_name GENERATE name, popularity;

dump out;

