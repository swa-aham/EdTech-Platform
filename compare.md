| Short Name           | API Endpoint                                                      | Time Before Indexing (ms) | Time After Indexing (ms) | Improvement (ms) | Improvement (%) |
|----------------------|-------------------------------------------------------------------|---------------------------|--------------------------|--------------------|-----------------|
| Weekly Scores        | GET /api/reports/weekly-scores?testId=1                           | 413                       | 208                      | 205                | 49.64%          |
| Monthly Scores       | GET /api/reports/monthly-scores?month=4&year=2025               | 1683                      | 871                      | 812                | 48.25%          |
| Overall Progress     | GET /api/reports/overall-progress?startTimestamp=...&endTimestamp=... | 7582                      | 4403                     | 3179               | 41.93%          |
| Monthly Attendance   | GET /api/reports/monthly-attendance?month=4&year=2025           | 1663                      | 562                      | 1101               | 66.21%          |
