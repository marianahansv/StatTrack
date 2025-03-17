import json
import random
from datetime import date, timedelta
import os

def generate_fake_data(n):
    """Generates fake goals and sections data with logical section assignments and a larger goal bank."""

    sections = ["Fitness", "Education", "Career", "Personal"]
    difficulties = ["Easy", "Medium", "Hard"]

    goals_data = {
        "Fitness": [
            "Run a Marathon",
            "Cook Healthy Meals",
            "Meditate Daily for 15 minutes",
            "Exercise 5 times a week",
            "Lose 10 pounds",
            "Complete a 30-day yoga challenge",
            "Learn to swim",
            "Hike a mountain",
            "Cycle 50 miles",
            "Improve flexibility",
            "Join a sports league",
            "Walk 10,000 steps daily",
            "Drink 8 glasses of water daily",
            "Get 8 hours of sleep",
            "Reduce screen time",
        ],
        "Education": [
            "Read 20 Books",
            "Learn Python and build a project",
            "Learn a new language to conversational level",
            "Complete 3 online courses",
            "Get a professional certification",
            "Learn a musical instrument",
            "Take a photography course",
            "Study a new subject",
            "Attend a workshop",
            "Improve public speaking skills",
            "Learn to code a website",
            "Read a technical book",
            "Get a master's degree",
            "Learn how to draw",
            "Take a writing class",
        ],
        "Career": [
            "Get a Promotion to senior level",
            "Network with 20 professionals in the industry",
            "Improve presentation and communication skills",
            "Complete a challenging project ahead of schedule",
            "Learn a new industry-specific skill",
            "Start a side hustle",
            "Attend an industry conference",
            "Create a professional portfolio",
            "Mentor a junior colleague",
            "Give a presentation at a conference",
            "Improve time management skills",
            "Learn a new software",
            "Negotiate a raise",
            "Get a new job",
            "Become a subject matter expert",
        ],
        "Personal": [
            "Save $5000 for a down payment",
            "Write a novel or a series of short stories",
            "Travel to 3 new countries",
            "Spend quality time with family every week",
            "Learn to play a new musical instrument fluently",
            "Volunteer at a local charity",
            "Learn to cook 10 new recipes",
            "Build a strong relationship with a friend",
            "Learn to garden",
            "Learn to paint or sculpt",
            "Learn to invest money",
            "Declutter your home",
            "Learn to sew or knit",
            "Start a journal",
            "Learn mindfulness techniques",
        ],
    }

    goals = []

    for section, titles in goals_data.items():
        if n >= 0:
            for title in titles:
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
        n -= 1

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
    fake_goals, sections = generate_fake_data(5)

    # Python equivalent of System.getProperty("user.home")
    user_home = os.path.expanduser("~")

    # Construct the file paths
    goals_file_path = os.path.join(user_home, "GoalApplication", "goals.json")
    sections_file_path = os.path.join(user_home, "GoalApplication", "sections.json")

    write_data_to_json(fake_goals, sections, goals_file_path, sections_file_path)
    print(f"Generated fake goals and sections and saved to {goals_file_path} and {sections_file_path}")