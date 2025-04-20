## Response Time comparison for non-indexed and indexed DB


| Request           | API Endpoint                                                      | Time Before Indexing (ms) | Time After Indexing (ms) | Improvement (ms) | Improvement (%) |
|----------------------|-------------------------------------------------------------------|---------------------------|--------------------------|--------------------|-----------------|
| Weekly Scores        | GET /api/reports/weekly-scores?testId=1                           | 413                       | 208                      | 205                | 49.64%          |
| Monthly Scores       | GET /api/reports/monthly-scores?month=4&year=2025               | 1683                      | 871                      | 812                | 48.25%          |
| Overall Progress     | GET /api/reports/overall-progress?startTimestamp=...&endTimestamp=... | 7582                      | 4403                     | 3179               | 41.93%          |
| Monthly Attendance   | GET /api/reports/monthly-attendance?month=4&year=2025           | 1663                      | 562                      | 1101               | 66.21%          |



## Response Time comparison after Redis cache integration


## Cache Miss Scenario (First Time)

| Request            | API Endpoint                                                      | Processing Time (ms) |
|--------------------|-------------------------------------------------------------------|----------------------|
| Weekly Scores      | GET /api/reports/weekly-scores?testId=1                           | 1054                 |
| Monthly Scores     | GET /api/reports/monthly-scores?month=4&year=2025               | 2663                 |
| Overall Progress   | GET /api/reports/overall-progress?startTimestamp=...&endTimestamp=... | 3996                 |
| Monthly Attendance | GET /api/reports/monthly-attendance?month=4&year=2025           | 1207                 |

## Cache Hit Scenario (Second Time)

| Request            | API Endpoint                                                      | Processing Time (ms) | Improvement (ms) | Improvement (%) |
|--------------------|-------------------------------------------------------------------|----------------------|--------------------|-----------------|
| Weekly Scores      | GET /api/reports/weekly-scores?testId=1                           | 55                   | 999                | 94.78%          |
| Monthly Scores     | GET /api/reports/monthly-scores?month=4&year=2025               | 14                   | 2649               | 99.47%          |
| Overall Progress   | GET /api/reports/overall-progress?startTimestamp=...&endTimestamp=... | 52                   | 3944               | 98.70%          |
| Monthly Attendance | GET /api/reports/monthly-attendance?month=4&year=2025           | 194                  | 1013               | 83.93%          |