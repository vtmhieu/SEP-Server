package group25.sep.server.service;

import group25.sep.server.model.RecruitmentRequest;
import group25.sep.server.model.enums.ContractType;
import group25.sep.server.model.enums.Department;
import group25.sep.server.model.enums.RecruitmentStatus;
import group25.sep.server.repository.RecruitmentRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit test for RecruitmentRequestServiceImpl
 * 
 * This test class demonstrates:
 * - How to test service layer methods
 * - How to use Mockito for mocking dependencies
 * - How to test different scenarios (success, failure, edge cases)
 * - How to verify method calls and interactions
 */
@ExtendWith(MockitoExtension.class)
class RecruitmentRequestServiceImplTest {

    // @Mock creates a mock object of RecruitmentRequestRepository
    // This means we don't use the real database, just a fake one for testing
    @Mock
    private RecruitmentRequestRepository repository;

    // @InjectMocks creates an instance of RecruitmentRequestServiceImpl
    // and injects the mocked repository into it
    @InjectMocks
    private RecruitmentRequestServiceImpl service;

    // Test data
    private RecruitmentRequest testRequest;
    private RecruitmentRequest savedRequest;

    /**
     * @BeforeEach runs before each test method
     *             This is where we set up our test data
     */
    @BeforeEach
    void setUp() {
        // Create a test recruitment request
        testRequest = RecruitmentRequest.builder()
                .contractType(ContractType.FULL_TIME)
                .requestingDepartment(Department.PRODUCTION)
                .yearsOfExperience(3)
                .jobTitle("Java Developer")
                .jobDescription("Looking for experienced Java developer")
                .status(RecruitmentStatus.OPEN)
                .build();

        // Create a saved version with ID (simulating what comes back from database)
        savedRequest = RecruitmentRequest.builder()
                .id(1L)
                .contractType(ContractType.FULL_TIME)
                .requestingDepartment(Department.PRODUCTION)
                .yearsOfExperience(3)
                .jobTitle("Java Developer")
                .jobDescription("Looking for experienced Java developer")
                .status(RecruitmentStatus.OPEN)
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * Test Case 1: Testing successful creation of recruitment request
     * 
     * What we're testing: createRecruitmentRequest() method
     * Scenario: Valid request is provided
     * Expected: Method should save and return the request
     */
    @Test
    void createRecruitmentRequest_WithValidRequest_ShouldReturnSavedRequest() {
        // ARRANGE (Given) - Set up the test scenario
        // Tell the mock repository what to return when save() is called
        when(repository.save(any(RecruitmentRequest.class))).thenReturn(savedRequest);

        // ACT (When) - Execute the method we want to test
        RecruitmentRequest result = service.createRecruitmentRequest(testRequest);

        // ASSERT (Then) - Verify the results
        // Check that the result is not null
        assertThat(result).isNotNull();

        // Check that the result has the correct ID (simulating database assignment)
        assertThat(result.getId()).isEqualTo(1L);

        // Check that the job title is correct
        assertThat(result.getJobTitle()).isEqualTo("Java Developer");

        // Check that the status is OPEN (default status)
        assertThat(result.getStatus()).isEqualTo(RecruitmentStatus.OPEN);

        // VERIFY - Check that the repository.save() method was called exactly once
        verify(repository, times(1)).save(testRequest);
    }

    /**
     * Test Case 2: Testing retrieval of recruitment request by ID
     * 
     * What we're testing: getRecruitmentRequestById() method
     * Scenario: Request exists in database
     * Expected: Method should return the request
     */
    @Test
    void getRecruitmentRequestById_WhenRequestExists_ShouldReturnRequest() {
        // ARRANGE
        Long requestId = 1L;
        when(repository.findById(requestId)).thenReturn(Optional.of(savedRequest));

        // ACT
        Optional<RecruitmentRequest> result = service.getRecruitmentRequestById(requestId);

        // ASSERT
        assertThat(result).isPresent(); // Check that Optional contains a value
        assertThat(result.get()).isEqualTo(savedRequest);
        assertThat(result.get().getJobTitle()).isEqualTo("Java Developer");

        // VERIFY
        verify(repository, times(1)).findById(requestId);
    }

    /**
     * Test Case 3: Testing retrieval when request doesn't exist
     * 
     * What we're testing: getRecruitmentRequestById() method
     * Scenario: Request doesn't exist in database
     * Expected: Method should return empty Optional
     */
    @Test
    void getRecruitmentRequestById_WhenRequestNotExists_ShouldReturnEmpty() {
        // ARRANGE
        Long nonExistentId = 999L;
        when(repository.findById(nonExistentId)).thenReturn(Optional.empty());

        // ACT
        Optional<RecruitmentRequest> result = service.getRecruitmentRequestById(nonExistentId);

        // ASSERT
        assertThat(result).isEmpty(); // Check that Optional is empty

        // VERIFY
        verify(repository, times(1)).findById(nonExistentId);
    }

    /**
     * Test Case 4: Testing retrieval of all recruitment requests
     * 
     * What we're testing: getAllRecruitmentRequests() method
     * Scenario: Multiple requests exist in database
     * Expected: Method should return list of all requests
     */
    @Test
    void getAllRecruitmentRequests_ShouldReturnAllRequests() {
        // ARRANGE
        List<RecruitmentRequest> mockRequests = Arrays.asList(
                savedRequest,
                RecruitmentRequest.builder()
                        .id(2L)
                        .jobTitle("DevOps Engineer")
                        .contractType(ContractType.PART_TIME)
                        .requestingDepartment(Department.SERVICES)
                        .yearsOfExperience(2)
                        .jobDescription("DevOps specialist needed")
                        .status(RecruitmentStatus.IN_PROGRESS)
                        .build());
        when(repository.findAll()).thenReturn(mockRequests);

        // ACT
        List<RecruitmentRequest> result = service.getAllRecruitmentRequests();

        // ASSERT
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getJobTitle()).isEqualTo("Java Developer");
        assertThat(result.get(1).getJobTitle()).isEqualTo("DevOps Engineer");

        // VERIFY
        verify(repository, times(1)).findAll();
    }

    /**
     * Test Case 5: Testing status update
     * 
     * What we're testing: updateRecruitmentRequestStatus() method
     * Scenario: Valid request ID and new status provided
     * Expected: Method should update and return the request
     */
    @Test
    void updateRecruitmentRequestStatus_WithValidId_ShouldUpdateAndReturnRequest() {
        // ARRANGE
        Long requestId = 1L;
        RecruitmentStatus newStatus = RecruitmentStatus.IN_PROGRESS;

        // Mock the repository to return the request when findById is called
        when(repository.findById(requestId)).thenReturn(Optional.of(savedRequest));
        // Mock the repository to return the updated request when save is called
        when(repository.save(any(RecruitmentRequest.class))).thenReturn(savedRequest);

        // ACT
        RecruitmentRequest result = service.updateRecruitmentRequestStatus(requestId, newStatus);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(newStatus);

        // VERIFY
        verify(repository, times(1)).findById(requestId);
        verify(repository, times(1)).save(any(RecruitmentRequest.class));
    }

    /**
     * Test Case 6: Testing status update when request doesn't exist
     * 
     * What we're testing: updateRecruitmentRequestStatus() method
     * Scenario: Request ID doesn't exist
     * Expected: Method should throw RuntimeException
     */
    @Test
    void updateRecruitmentRequestStatus_WhenRequestNotExists_ShouldThrowException() {
        // ARRANGE
        Long nonExistentId = 999L;
        RecruitmentStatus newStatus = RecruitmentStatus.IN_PROGRESS;
        when(repository.findById(nonExistentId)).thenReturn(Optional.empty());

        // ACT & ASSERT
        // Use assertThatThrownBy to test that an exception is thrown
        assertThatThrownBy(() -> service.updateRecruitmentRequestStatus(nonExistentId, newStatus))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Recruitment request not found with id: " + nonExistentId);

        // VERIFY
        verify(repository, times(1)).findById(nonExistentId);
        verify(repository, never()).save(any()); // Should never call save if request not found
    }

    /**
     * Test Case 7: Testing HR notes update
     * 
     * What we're testing: updateHrNotes() method
     * Scenario: Valid request ID and notes provided
     * Expected: Method should update notes and return the request
     */
    @Test
    void updateHrNotes_WithValidId_ShouldUpdateNotesAndReturnRequest() {
        // ARRANGE
        Long requestId = 1L;
        String hrNotes = "Interview scheduled for next week";
        when(repository.findById(requestId)).thenReturn(Optional.of(savedRequest));
        when(repository.save(any(RecruitmentRequest.class))).thenReturn(savedRequest);

        // ACT
        RecruitmentRequest result = service.updateHrNotes(requestId, hrNotes);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getHrNotes()).isEqualTo(hrNotes);

        // VERIFY
        verify(repository, times(1)).findById(requestId);
        verify(repository, times(1)).save(any(RecruitmentRequest.class));
    }
}