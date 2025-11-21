---
mode: 'agent'
description: 'Create a README.md file for a project based on provided details.'
inputs:
  - project_name: 'Java Interview - Setup'
    description: 'A sample project to verify local development environment setup for a live coding session.'
  - prerequisites:          
----
## ROLE 'The prerequisites needed for the project setup.'


------
    - 'Java 17+'
    - 'Maven'
  - setup_steps:
    - 'Clone this repository.'
    - 'Open the project in your preferred IDE.'
    - 'Build the project using Maven.'
    - 'Run the tests to ensure everything is functioning properly.'
outputs:
    - readme_content: 'The content of the README.md file.'
    - file_path: 'The file path where the README.md should be saved.'
    - file_name: 'The name of the README file.'
    - file_extension: 'The file extension of the README file.'
    - summary: 'A brief summary of the README content.'
    - tags: 'Relevant tags for the README file.'
    - 'The README.md file should include the project name, description, prerequisites, and setup steps in a clear and organized manner.'
----
