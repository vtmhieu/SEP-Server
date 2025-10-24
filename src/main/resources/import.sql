INSERT INTO EVENT (name, location, event_date, type, description, estimate_budget, status) VALUES ('Tech Summit 2025', 'Negombo', '2025-12-15', 'type1', 'Annual technology conference', '100.0', 'PLANNED');

INSERT INTO EVENT (name, location, event_date, type, description, estimate_budget, status) VALUES ('AI Expo', 'Colombo', '2025-11-20', 'type1', 'AI and Data Science Exhibition', '200.0', 'ONGOING');

INSERT INTO EVENT (name, location, event_date, type, description, estimate_budget, status) VALUES ('Developer Meetup', 'Kandy', '2025-10-30', 'type1', 'Regional developer community meetup', '130.0', 'COMPLETED');

-- Sample Recruitment Requests
INSERT INTO RECRUITMENT_REQUESTS (contract_type, requesting_department, years_of_experience, job_title, job_description, status, hr_notes, created_at, updated_at) VALUES ('FULL_TIME', 'PRODUCTION', 3, 'Senior Java Developer', 'Looking for an experienced Java developer with Spring Boot knowledge for our production team', 'OPEN', NULL, '2025-01-15 10:00:00', NULL);

INSERT INTO RECRUITMENT_REQUESTS (contract_type, requesting_department, years_of_experience, job_title, job_description, status, hr_notes, created_at, updated_at) VALUES ('PART_TIME', 'SERVICES', 2, 'DevOps Engineer', 'Need a DevOps specialist for cloud infrastructure management on part-time basis', 'IN_PROGRESS', 'Interview scheduled for next week', '2025-01-16 09:30:00', '2025-01-16 14:20:00');

INSERT INTO RECRUITMENT_REQUESTS (contract_type, requesting_department, years_of_experience, job_title, job_description, status, hr_notes, created_at, updated_at) VALUES ('FULL_TIME', 'PRODUCTION', 5, 'Frontend Developer', 'React.js developer needed for UI development with 5+ years experience', 'CLOSED', 'Position filled successfully', '2025-01-14 11:15:00', '2025-01-17 16:45:00');

-- Sample Financial Requests
INSERT INTO FINANCIAL_REQUESTS (requesting_department, project_reference, required_amount, currency, reason, status, fm_notes, created_at, updated_at) VALUES ('PRODUCTION', 'PROJ-001', 5000.00, 'USD', 'New development laptops for production team', 'PENDING', NULL, '2025-01-15 08:00:00', NULL);

INSERT INTO FINANCIAL_REQUESTS (requesting_department, project_reference, required_amount, currency, reason, status, fm_notes, created_at, updated_at) VALUES ('SERVICES', 'PROJ-002', 2500.00, 'USD', 'Conference attendance for team members', 'APPROVED', 'Approved based on Q1 budget allocation', '2025-01-16 10:30:00', '2025-01-16 15:10:00');

INSERT INTO FINANCIAL_REQUESTS (requesting_department, project_reference, required_amount, currency, reason, status, fm_notes, created_at, updated_at) VALUES ('PRODUCTION', 'PROJ-003', 10000.00, 'USD', 'Additional budget for project expansion', 'REJECTED', 'Exceeds budget limits for this quarter', '2025-01-14 14:20:00', '2025-01-17 11:30:00');