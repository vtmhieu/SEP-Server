package group25.sep.server.service;

import group25.sep.server.model.FinancialRequest;
import group25.sep.server.model.enums.Department;
import group25.sep.server.model.enums.FinancialRequestStatus;
import group25.sep.server.repository.FinancialRequestRepository;
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

@ExtendWith(MockitoExtension.class)
class FinancialRequestServiceImplTest {

    @Mock // A mock meaning we don't use the real database
    private FinancialRequestRepository repository;

    // creates an instances of the FinancialRequestServiceImpl and injects the mocks
    // into it
    // and injects the mock repository above into it
    @InjectMocks
    private FinancialRequestServiceImpl service;

    // Test data for later use
    private FinancialRequest testRequest;
    private FinancialRequest savedRequest;

    // Setup test data before each test
    @BeforeEach
    void setUp() {
        // create testRequest and savedRequest
        testRequest = FinancialRequest.builder()
                .projectReference("PRJ-001")
                .reason("Need budget for new project")
                .requestingDepartment(Department.PRODUCTION)
                .requiredAmount(new java.math.BigDecimal("5000.00"))
                .status(FinancialRequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        savedRequest = FinancialRequest.builder()
                .id(1L)
                .projectReference("PRJ-001")
                .reason("Need budget for new project")
                .requestingDepartment(Department.PRODUCTION)
                .requiredAmount(new java.math.BigDecimal("5000.00"))
                .status(FinancialRequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
    }

    // Test case 1: test creating a financial request
    @Test
    void testCreateFinancialRequest() {
        when(repository.save(any(FinancialRequest.class))).thenReturn(savedRequest);

        FinancialRequest result = service.createFinancialRequest(testRequest);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getProjectReference()).isEqualTo("PRJ-001");

        verify(repository, times(1)).save(testRequest);
    }

    // Test case 2: test getting a financial request by ID
    @Test
    void testGetFinancialRequestById() {
        when(repository.findById(1L)).thenReturn(Optional.of(savedRequest));

        Optional<FinancialRequest> result = service.getFinancialRequestById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);

        verify(repository, times(1)).findById(1L);
    }

    // Test case 3: test getting all financial requests
    @Test
    void testGetAllFinancialRequests() {
        when(repository.findAll()).thenReturn(Arrays.asList(savedRequest));
    }

    // Test case 4: test retrieval when request doesn't exists
    @Test
    void testGetFinancialRequestById_NotFound() {
        when(repository.findById(2L)).thenReturn(Optional.empty());

        Optional<FinancialRequest> result = service.getFinancialRequestById(2L);

        assertThat(result).isNotPresent();

        verify(repository, times(1)).findById(2L);
    }

    // Test case 5: test status update
    @Test
    void testUpdateFinancialRequestStatus() {
        when(repository.findById(1L)).thenReturn(Optional.of(savedRequest));
        when(repository.save(any(FinancialRequest.class))).thenReturn(savedRequest);

        FinancialRequest result = service.updateFinancialRequestStatus(1L, FinancialRequestStatus.APPROVED);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(FinancialRequestStatus.APPROVED);

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(savedRequest);
    }

    // Test case 6: test status update when request doesn't exist
    @Test
    void testUpdateFinancialRequestStatus_NotFound() {
        when(repository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateFinancialRequestStatus(2L, FinancialRequestStatus.APPROVED))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Financial request not found with id: 2");

        verify(repository, times(1)).findById(2L);
    }

    // Test case 7: test deletion of a financial request
    @Test
    void testDeleteFinancialRequest() {
        when(repository.findById(1L)).thenReturn(Optional.of(savedRequest));
        doNothing().when(repository).deleteById(1L);

        service.deleteFinancialRequest(1L);

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    // Test case 8: test deletion when request doesn't exist
    @Test
    void testDeleteFinancialRequest_NotFound() {
        when(repository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteFinancialRequest(2L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Financial request not found with id: 2");

        verify(repository, times(1)).findById(2L);
    }

    // Test case 9: test getting financial requests by status
    @Test
    void testGetFinancialRequestsByStatus() {
        when(repository.findByStatus(FinancialRequestStatus.PENDING))
                .thenReturn(Arrays.asList(savedRequest));

        List<FinancialRequest> result = service.getFinancialRequestsByStatus(FinancialRequestStatus.PENDING);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(FinancialRequestStatus.PENDING);

        verify(repository, times(1)).findByStatus(FinancialRequestStatus.PENDING);
    }
}
