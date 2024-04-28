import com.google.gson.annotations.SerializedName

data class Expense(
    @SerializedName("id") val id: Int?,
    @SerializedName("startDateTime") val startDateTime: String?,
    @SerializedName("endDateTime") val endDateTime: String?,
    @SerializedName("state") val state: String? = null,
    @SerializedName("userId") val userId: Long?,
    @SerializedName("projectId") val projectId: Long?,
    @SerializedName("weeklyTimecardId") val weeklyTimecardId: Long?,
    @SerializedName("description") val description: String = "Test Description"
)