// Career Guidance Platform - Dashboard JavaScript

document.addEventListener('DOMContentLoaded', function() {
    initializeDashboard();
    loadDashboardData();
    setupEventListeners();
});

function initializeDashboard() {
    // Add fade-in animation to cards
    const cards = document.querySelectorAll('.card');
    cards.forEach((card, index) => {
        card.style.opacity = '0';
        card.style.transform = 'translateY(20px)';
        
        setTimeout(() => {
            card.style.transition = 'all 0.5s ease';
            card.style.opacity = '1';
            card.style.transform = 'translateY(0)';
        }, index * 100);
    });
    
    // Initialize tooltips
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
    
    // Initialize progress bars with animation
    animateProgressBars();
    
    // Setup auto-refresh for dynamic content
    setupAutoRefresh();
}

function loadDashboardData() {
    // Load recent activities
    loadRecentActivities();
    
    // Load upcoming events
    loadUpcomingEvents();
    
    // Load notifications
    loadNotifications();
    
    // Update statistics
    updateStatistics();
}

function setupEventListeners() {
    // Quick action buttons
    const quickActionBtns = document.querySelectorAll('.quick-action-btn');
    quickActionBtns.forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.preventDefault();
            const action = this.getAttribute('data-action');
            handleQuickAction(action);
        });
    });
    
    // Assessment cards
    const assessmentCards = document.querySelectorAll('.assessment-card');
    assessmentCards.forEach(card => {
        card.addEventListener('click', function() {
            const assessmentId = this.getAttribute('data-assessment-id');
            if (assessmentId) {
                showAssessmentDetails(assessmentId);
            }
        });
    });
    
    // Recommendation cards
    const recommendationCards = document.querySelectorAll('.recommendation-card');
    recommendationCards.forEach(card => {
        card.addEventListener('click', function() {
            const recommendationId = this.getAttribute('data-recommendation-id');
            if (recommendationId) {
                showRecommendationDetails(recommendationId);
            }
        });
    });
    
    // Refresh button
    const refreshBtn = document.getElementById('refreshDashboard');
    if (refreshBtn) {
        refreshBtn.addEventListener('click', function() {
            refreshDashboard();
        });
    }
}

function handleQuickAction(action) {
    switch(action) {
        case 'take-assessment':
            window.location.href = '/assessment-form';
            break;
        case 'explore-careers':
            window.location.href = '/career-paths';
            break;
        case 'find-mentor':
            window.location.href = '/mentor-request';
            break;
        case 'view-progress':
            window.location.href = '/progress-summary';
            break;
        default:
            console.log('Unknown action:', action);
    }
}

function showAssessmentDetails(assessmentId) {
    // Show loading state
    showLoadingModal('Loading assessment details...');
    
    // In a real application, this would fetch assessment details from API
    setTimeout(() => {
        hideLoadingModal();
        
        const modalHtml = `
            <div class="modal fade" id="assessmentDetailsModal" tabindex="-1">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Assessment Details</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <div class="text-center">
                                <i class="fas fa-clipboard-check fa-3x text-primary mb-3"></i>
                                <h5>Assessment ID: ${assessmentId}</h5>
                                <p class="text-muted">Detailed assessment information would be displayed here.</p>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            <button type="button" class="btn btn-primary" onclick="startAssessment('${assessmentId}')">
                                Start Assessment
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        `;
        
        document.body.insertAdjacentHTML('beforeend', modalHtml);
        const modal = new bootstrap.Modal(document.getElementById('assessmentDetailsModal'));
        modal.show();
        
        // Clean up modal after it's hidden
        document.getElementById('assessmentDetailsModal').addEventListener('hidden.bs.modal', function() {
            this.remove();
        });
    }, 1000);
}

function showRecommendationDetails(recommendationId) {
    // Similar implementation for recommendation details
    console.log('Showing recommendation details for ID:', recommendationId);
}

function animateProgressBars() {
    const progressBars = document.querySelectorAll('.progress-bar');
    
    progressBars.forEach(bar => {
        const targetWidth = bar.style.width || bar.getAttribute('aria-valuenow') + '%';
        bar.style.width = '0%';
        
        setTimeout(() => {
            bar.style.transition = 'width 1s ease-in-out';
            bar.style.width = targetWidth;
        }, 500);
    });
}

function loadRecentActivities() {
    const activitiesContainer = document.getElementById('recentActivities');
    if (!activitiesContainer) return;
    
    // Show loading state
    activitiesContainer.innerHTML = '<div class="text-center"><div class="spinner"></div></div>';
    
    // Simulate API call
    setTimeout(() => {
        const activities = [
            {
                icon: 'fas fa-clipboard-check',
                title: 'Completed Java Programming Assessment',
                time: '2 hours ago',
                type: 'success'
            },
            {
                icon: 'fas fa-user-tie',
                title: 'New mentorship request from John Doe',
                time: '1 day ago',
                type: 'info'
            },
            {
                icon: 'fas fa-trophy',
                title: 'Earned "Quick Learner" badge',
                time: '3 days ago',
                type: 'warning'
            }
        ];
        
        let activitiesHtml = '';
        activities.forEach(activity => {
            activitiesHtml += `
                <div class="d-flex align-items-center mb-3">
                    <div class="flex-shrink-0">
                        <i class="${activity.icon} fa-lg text-${activity.type}"></i>
                    </div>
                    <div class="flex-grow-1 ms-3">
                        <h6 class="mb-1">${activity.title}</h6>
                        <small class="text-muted">${activity.time}</small>
                    </div>
                </div>
            `;
        });
        
        activitiesContainer.innerHTML = activitiesHtml;
    }, 1000);
}

function loadUpcomingEvents() {
    const eventsContainer = document.getElementById('upcomingEvents');
    if (!eventsContainer) return;
    
    // Show loading state
    eventsContainer.innerHTML = '<div class="text-center"><div class="spinner"></div></div>';
    
    // Simulate API call
    setTimeout(() => {
        const events = [
            {
                title: 'Mentorship Session with Sarah Johnson',
                date: 'Tomorrow, 2:00 PM',
                type: 'meeting'
            },
            {
                title: 'Data Science Assessment Deadline',
                date: 'Dec 15, 2023',
                type: 'deadline'
            },
            {
                title: 'Career Fair Registration Opens',
                date: 'Dec 20, 2023',
                type: 'event'
            }
        ];
        
        let eventsHtml = '';
        events.forEach(event => {
            const iconClass = event.type === 'meeting' ? 'fas fa-video' : 
                             event.type === 'deadline' ? 'fas fa-clock' : 'fas fa-calendar';
            
            eventsHtml += `
                <div class="d-flex align-items-center mb-3">
                    <div class="flex-shrink-0">
                        <i class="${iconClass} fa-lg text-primary"></i>
                    </div>
                    <div class="flex-grow-1 ms-3">
                        <h6 class="mb-1">${event.title}</h6>
                        <small class="text-muted">${event.date}</small>
                    </div>
                </div>
            `;
        });
        
        eventsContainer.innerHTML = eventsHtml;
    }, 1200);
}

function loadNotifications() {
    const notificationBadge = document.getElementById('notificationBadge');
    const notificationCount = document.getElementById('notificationCount');
    
    // Simulate API call to get notification count
    setTimeout(() => {
        const count = Math.floor(Math.random() * 5) + 1; // Random count between 1-5
        
        if (notificationBadge && notificationCount) {
            notificationCount.textContent = count;
            notificationBadge.style.display = count > 0 ? 'inline' : 'none';
        }
    }, 800);
}

function updateStatistics() {
    const statElements = document.querySelectorAll('.stat-number');
    
    statElements.forEach(element => {
        const targetValue = parseInt(element.textContent);
        let currentValue = 0;
        const increment = targetValue / 50; // Animate over 50 steps
        
        const timer = setInterval(() => {
            currentValue += increment;
            if (currentValue >= targetValue) {
                currentValue = targetValue;
                clearInterval(timer);
            }
            element.textContent = Math.floor(currentValue);
        }, 30);
    });
}

function setupAutoRefresh() {
    // Refresh dashboard data every 5 minutes
    setInterval(() => {
        loadRecentActivities();
        loadUpcomingEvents();
        loadNotifications();
    }, 300000); // 5 minutes
}

function refreshDashboard() {
    const refreshBtn = document.getElementById('refreshDashboard');
    if (refreshBtn) {
        refreshBtn.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Refreshing...';
        refreshBtn.disabled = true;
    }
    
    // Reload all dashboard data
    loadDashboardData();
    
    setTimeout(() => {
        if (refreshBtn) {
            refreshBtn.innerHTML = '<i class="fas fa-sync me-2"></i>Refresh';
            refreshBtn.disabled = false;
        }
        
        showToast('Dashboard refreshed successfully!', 'success');
    }, 2000);
}

function showLoadingModal(message = 'Loading...') {
    const modalHtml = `
        <div class="modal fade" id="loadingModal" tabindex="-1" data-bs-backdrop="static">
            <div class="modal-dialog modal-sm modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-body text-center py-4">
                        <div class="spinner mb-3"></div>
                        <p class="mb-0">${message}</p>
                    </div>
                </div>
            </div>
        </div>
    `;
    
    document.body.insertAdjacentHTML('beforeend', modalHtml);
    const modal = new bootstrap.Modal(document.getElementById('loadingModal'));
    modal.show();
}

function hideLoadingModal() {
    const loadingModal = document.getElementById('loadingModal');
    if (loadingModal) {
        const modal = bootstrap.Modal.getInstance(loadingModal);
        if (modal) {
            modal.hide();
            setTimeout(() => loadingModal.remove(), 300);
        }
    }
}

function showToast(message, type = 'info') {
    const toastHtml = `
        <div class="toast align-items-center text-white bg-${type} border-0" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="d-flex">
                <div class="toast-body">
                    ${message}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
            </div>
        </div>
    `;
    
    let toastContainer = document.getElementById('toastContainer');
    if (!toastContainer) {
        toastContainer = document.createElement('div');
        toastContainer.id = 'toastContainer';
        toastContainer.className = 'toast-container position-fixed bottom-0 end-0 p-3';
        document.body.appendChild(toastContainer);
    }
    
    toastContainer.insertAdjacentHTML('beforeend', toastHtml);
    const toastElement = toastContainer.lastElementChild;
    const toast = new bootstrap.Toast(toastElement);
    toast.show();
    
    // Remove toast element after it's hidden
    toastElement.addEventListener('hidden.bs.toast', function() {
        this.remove();
    });
}

function startAssessment(assessmentId) {
    showLoadingModal('Starting assessment...');
    
    setTimeout(() => {
        hideLoadingModal();
        window.location.href = `/assessment/${assessmentId}/start`;
    }, 1500);
}

// Utility functions
function formatDate(date) {
    return new Intl.DateTimeFormat('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    }).format(new Date(date));
}

function formatNumber(num) {
    return new Intl.NumberFormat('en-US').format(num);
}

function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// Export functions for global use
window.dashboardUtils = {
    showToast,
    showLoadingModal,
    hideLoadingModal,
    refreshDashboard,
    startAssessment,
    formatDate,
    formatNumber
};