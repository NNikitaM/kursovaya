package ru.demo.investmentapp.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "reports", schema = "public")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long reportId;

    @Column(name = "report_type", nullable = false)
    private String reportType;

    @Column(name = "report_date", nullable = false)
    private java.sql.Date reportDate;

    @Column(name = "report_content", nullable = true)
    private String reportContent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_name", nullable = false)
    private User user;

    public Report() {
    }

    public Report(String reportType, java.sql.Date reportDate, String reportContent, User user) {
        this.reportType = reportType;
        this.reportDate = reportDate;
        this.reportContent = reportContent;
        this.user = user;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public java.sql.Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(java.sql.Date reportDate) {
        this.reportDate = reportDate;
    }

    public String getReportContent() {
        return reportContent;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Report report)) return false;
        return Objects.equals(reportId, report.reportId) &&
                Objects.equals(reportType, report.reportType) &&
                Objects.equals(reportDate, report.reportDate) &&
                Objects.equals(reportContent, report.reportContent) &&
                Objects.equals(user, report.user);
    }

    @Override
    public int hashCode() {
        //return Objects.hash(reportId, reportType, reportDate, reportContent, user);
        final int hashCode = 31 * reportType.hashCode() + 17 * reportDate.hashCode() +
                17 * user.getUserName().hashCode() + 17 * reportContent.hashCode() + 17 * reportId.hashCode();
        return hashCode;
    }
}
