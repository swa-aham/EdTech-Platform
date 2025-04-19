-- Index for /api/reports/weekly-scores?testId=1
ALTER TABLE test_submissions ADD INDEX idx_testid (testid);

-- Adding generated columns and index for /api/reports/monthly-scores?month=4&year=2025
ALTER TABLE test_submissions 
    ADD COLUMN submission_year INT GENERATED ALWAYS AS (YEAR(submission_date)) STORED,
    ADD COLUMN submission_month INT GENERATED ALWAYS AS (MONTH(submission_date)) STORED;

ALTER TABLE test_submissions 
    ADD INDEX idx_submission_month_year (submission_year, submission_month);

-- Adding generated columns and index for /api/reports/monthly-attendance?month=4&year=2025
ALTER TABLE attendance 
    ADD COLUMN attendance_year INT GENERATED ALWAYS AS (YEAR(attendance_date)) STORED,
    ADD COLUMN attendance_month INT GENERATED ALWAYS AS (MONTH(attendance_date)) STORED;

ALTER TABLE attendance 
    ADD INDEX idx_attendance_month_year (attendance_year, attendance_month);

-- Index for /api/reports/overall-progress?startTimestamp=2025-01-01T00:00:00&endTimestamp=2025-04-30T23:59:59
ALTER TABLE test_submissions ADD INDEX idx_submissiondate (submission_date);
