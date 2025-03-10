import json
import random
from datetime import date, timedelta
import os

def generate_fake_data():
    """Generates fake goals and sections data."""

    sections = ["Fitness", "Education", "Career", "Personal"]
    difficulties = ["Easy", "Medium", "Hard"]
    titles = [
        "Read 10 Books",
        "Learn Python",
        "Run a Marathon",
        "Save $1000",
        "Meditate Daily",
        "Write a Novel",
        "Learn a Language",
        "Get a Promotion",
        "Cook Healthy Meals",
        "Travel Abroad",
    ]

    goals = []
    for _ in range(len(titles)):
        title = random.choice(titles)
        section = random.choice(sections)
        difficulty = random.choice(difficulties)
        start_date = date.today() + timedelta(days=random.randint(-30, 30))
        end_date = start_date + timedelta(days=random.randint(1, 30))

        goal = {
            "title": title,
            "section": section,
            "difficulty": difficulty,
            "startDate": start_date.strftime("%Y-%m-%d"),
            "endDate": end_date.strftime("%Y-%m-%d"),
        }
        goals.append(goal)

    return goals, sections

def write_data_to_json(goals, sections, goals_filepath, sections_filepath):
    """Writes goals and sections data to specified file paths."""

    # Ensure the directories exist
    os.makedirs(os.path.dirname(goals_filepath), exist_ok=True)
    os.makedirs(os.path.dirname(sections_filepath), exist_ok=True)

    with open(goals_filepath, "w") as goals_file:
        json.dump(goals, goals_file, indent=4)

    with open(sections_filepath, "w") as sections_file:
        json.dump(sections, sections_file, indent=4)

if __name__ == "__main__":
    fake_goals, sections = generate_fake_data()

    # Python equivalent of System.getProperty("user.home")
    user_home = os.path.expanduser("~")

    # Construct the file paths
    goals_file_path = os.path.join(user_home, "GoalApplication", "goals.json")
    sections_file_path = os.path.join(user_home, "GoalApplication", "sections.json")

    write_data_to_json(fake_goals, sections, goals_file_path, sections_file_path)
    print(f"Generated fake goals and sections and saved to {goals_file_path} and {sections_file_path}")