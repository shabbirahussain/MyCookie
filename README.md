# MyCookie

## Summary

Given a csv file of cookies, and a date finds the most active cookie on that date, counting cookies in `UTC`.

## Input file format
### CSV file constraints
* CSV file must consist of only two fields and is expected to be comma separated 
* CSV must not be quoted and must contain fields in below format
    * First field is the cookie id and 
    * Second field must adhere to datetime of format `yyyy-MM-dd'T'HH:mm:ssZZ`. E.g.:`2018-12-08T09:30:00+00:00` 
* CSV is expected to have a header row which is skipped.

> Note: Malformed inputs are skipped.

## How to execute
To execute use `jar` from the latest release.

```shell
java -jar my_cookie.jar -f=<file.csv> -d=<YYYY-MM-DD>
```
