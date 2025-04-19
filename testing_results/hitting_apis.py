import requests
import time
from datetime import datetime

api_endpoints = [
    {"name": "Weekly Scores", "url": "http://localhost:8080/api/reports/weekly-scores?testId=1"},
    {"name": "Monthly Scores", "url": "http://localhost:8080/api/reports/monthly-scores?month=4&year=2025"},
    {"name": "Overall Progress", "url": "http://localhost:8080/api/reports/overall-progress?startTimestamp=2025-01-01T00:00:00&endTimestamp=2025-04-30T23:59:59"},
    {"name": "Monthly Attendance", "url": "http://localhost:8080/api/reports/monthly-attendance?month=4&year=2025"},
]

print(f"Starting API request execution at: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}\n")

for endpoint in api_endpoints:
    name = endpoint["name"]
    url = endpoint["url"]

    print(f"Hitting API: {name} - {url}")
    start_time = time.time()
    try:
        response = requests.get(url)
        response.raise_for_status()  # Raise an exception for bad status codes
        end_time = time.time()
        duration = end_time - start_time
        data = response.json()
        print(f"  Status Code: {response.status_code}")
        # print(f"  Response Data: {data}")
        print(f"  Time Taken: {duration:.4f} seconds\n")
    except requests.exceptions.RequestException as e:
        end_time = time.time()
        duration = end_time - start_time
        print(f"  Error: Request failed - {e}")
        print(f"  Time Taken: {duration:.4f} seconds\n")
    except ValueError:
        print(f"  Error: Could not decode JSON response.\n")

print(f"Finished API request execution at: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")