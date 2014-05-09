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

-- Calculates delays
delays = FOREACH data GENERATE satime, day AS d, month AS m, year AS y, (int)(arrtime-satime) AS delay;

-- Hour statistics
hourly = GROUP delays BY satime/100; -- from hhmm to hh
hourly_count = FOREACH hourly {
	delays_15 = FILTER delays BY (delay >= 15);
	GENERATE group, COUNT(delays) AS tot, COUNT(delays_15) AS del, (float) COUNT(delays_15)/COUNT(delays) AS frac;
}
STORE hourly_count INTO './local-output/AIR_3_HOURLY/';

-- Day statistics
daily = GROUP delays BY d;
daily_count = FOREACH daily {
	delays_15 = FILTER delays BY (delay >= 15);
	GENERATE group, COUNT(delays) AS tot, COUNT(delays_15) AS del, (float) COUNT(delays_15)/COUNT(delays) AS frac;
}
STORE daily_count INTO './local-output/AIR_3_DAILY/';

-- Month statistic
monthly = GROUP delays BY m;
monthly_count = FOREACH monthly {
	delays_15 = FILTER delays BY (delay >= 15);
	GENERATE group, COUNT(delays) AS tot, COUNT(delays_15) AS del, (float) COUNT(delays_15)/COUNT(delays) AS frac;
}
STORE monthly_count INTO './local-output/AIR_3_MONTLY/';

-- Year statistic
years = GROUP delays BY y;
years_count = FOREACH years {
	delays_15 = FILTER delays BY (delay >= 15);
	GENERATE group, COUNT(delays) AS tot, COUNT(delays_15) AS del, (float) COUNT(delays_15)/COUNT(delays) AS frac;
}
STORE years_count INTO './local-output/AIR_3_YEARLY/';
