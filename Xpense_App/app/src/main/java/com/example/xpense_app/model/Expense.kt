import com.google.gson.annotations.SerializedName

data class Expense(
    @SerializedName("id") val id: Long?,
    @SerializedName("startDateTime") val startDateTime: String?,
    @SerializedName("endDateTime") val endDateTime: String?,
    @SerializedName("state") val state: String? = null,
    @SerializedName("userId") val userId: Long?,
    @SerializedName("projectId") val projectId: Long?,
    @SerializedName("description") val description: String? = "",
    @SerializedName("pausedAtTimestamp") val pausedAtTimestamp: Long? = 0L
)