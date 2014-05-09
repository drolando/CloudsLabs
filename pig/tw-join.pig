-- set default parallel to 20
set default_parallel 40;

-- TODO: load the input dataset, located in ./local-input/OSN/tw.txt
A = LOAD '/laboratory/input/twitter-big-sample.txt' AS (id: long, fr: long);
B = LOAD '/laboratory/input/twitter-big-sample.txt' AS (id: long, fr: long);

SPLIT A INTO clean_dataA IF id is not null AND fr is not null, trash_data OTHERWISE;
SPLIT B INTO clean_dataB IF id is not null AND fr is not null, trash_data OTHERWISE;

-- TODO: compute all the two-hop paths 
twohop = JOIN clean_dataA by fr, clean_dataB by id;

-- TODO: project the twohop relation such that in output you display only the start and end nodes of the two hop path
p_result = FOREACH twohop GENERATE $0,$3;

-- remove duplicates
result_distinct = DISTINCT p_result;

-- TODO: make sure you avoid loops (e.g., if user 12 and 13 follow eachother) 
-- This means that from (12,13) and (13,12) I get (12,12) -> it's enough to delete pairs with the same ids
result = FILTER result_distinct BY $0!=$1;

--DUMP result;
STORE result INTO './PIG/OSN/twj/';
