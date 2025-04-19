import mysql.connector
import random
from faker import Faker
from datetime import datetime, timedelta

# Create connection to MySQL
conn = mysql.connector.connect(
    host="localhost",
    user="root",  # Update with your MySQL username
    password="password",  # Update with your MySQL password
    database="online_learning_school",
)

cursor = conn.cursor()

# Initialize Faker for generating fake data
fake = Faker()

# Insert 1000 unique records into Users table (students and teachers)
roles = ["student", "teacher"]
for _ in range(1000):
    first_name = fake.first_name()
    last_name = fake.last_name()
    role = roles[0] if _ < 800 else roles[1]  # 800 students and 200 teachers

    cursor.execute(
        """
        INSERT INTO users (role, first_name, last_name)
        VALUES (%s, %s, %s)
    """,
        (role, first_name, last_name),
    )

# Commit user insertions
conn.commit()

# Insert 1000 unique records into Courses table
for _ in range(1000):
    course_name = fake.job().capitalize()  # Use unique job titles as course names

    cursor.execute(
        """
        INSERT INTO courses (course_name)
        VALUES (%s)
    """,
        (course_name,),
    )

# Commit course insertions
conn.commit()

# Insert 1000 unique records into WeeklyTests table
for _ in range(1000):
    courseid = random.randint(
        1, 1000
    )  # Randomly assign tests to any of the 1000 courses
    test_name = f"{fake.word().capitalize()} Test"
    date_created = fake.date_this_year()

    cursor.execute(
        """
        INSERT INTO weekly_tests (courseid, test_name, date_created)
        VALUES (%s, %s, %s)
    """,
        (courseid, test_name, date_created),
    )

# Commit test insertions
conn.commit()

# Insert 1000 unique records into TestSubmissions table
for _ in range(1000):
    test_id = random.randint(1, 1000)  # Assuming there are 1000 tests
    user_id = random.randint(1, 800)  # Only students submit tests
    score = round(random.uniform(50.0, 100.0), 2)
    submission_date = fake.date_time_this_year()

    cursor.execute(
        """
        INSERT INTO test_submissions (testid, userid, score, submission_date)
        VALUES (%s, %s, %s, %s)
    """,
        (test_id, user_id, score, submission_date),
    )

# Commit test submission insertions
conn.commit()

# Insert 1000 unique records into Attendance table
for _ in range(1000):
    user_id = random.randint(1, 800)  # Only students are recorded for attendance
    attendance_date = (datetime.now() - timedelta(days=random.randint(1, 30))).date()
    status = random.choice(["present", "absent", "late"])

    cursor.execute(
        """
        INSERT INTO attendance (userid, attendance_date, status)
        VALUES (%s, %s, %s)
    """,
        (user_id, attendance_date, status),
    )

# Commit attendance insertions
conn.commit()

# Close connection
cursor.close()
conn.close()

print(
    "1000 unique records inserted into Users, Courses, WeeklyTests, TestSubmissions, and Attendance tables."
)
