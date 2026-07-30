package codewithkk.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatsResponse {
    private long totalUsers;
    private long totalNotes;
    private long totalPurchases;
    private double totalRevenue;
    private long activeNotes;
}
