
package com.roadstar.customerr.app.data.models.booking_status;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookingStatus {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("booking_id")
    @Expose
    private String bookingId;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("provider_id")
    @Expose
    private int providerId;
    @SerializedName("current_provider_id")
    @Expose
    private int currentProviderId;
    @SerializedName("service_type_id")
    @Expose
    private int serviceTypeId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("cancelled_by")
    @Expose
    private String cancelledBy;
    @SerializedName("cancel_reason")
    @Expose
    private Object cancelReason;
    @SerializedName("payment_mode")
    @Expose
    private String paymentMode;
    @SerializedName("paid")
    @Expose
    private int paid;
    @SerializedName("is_track")
    @Expose
    private String isTrack;
    @SerializedName("distance")
    @Expose
    private int distance;
    @SerializedName("travel_time")
    @Expose
    private Object travelTime;
    @SerializedName("s_address")
    @Expose
    private String sAddress;
    @SerializedName("s_latitude")
    @Expose
    private double sLatitude;
    @SerializedName("s_longitude")
    @Expose
    private double sLongitude;
    @SerializedName("d_address")
    @Expose
    private String dAddress;
    @SerializedName("d_latitude")
    @Expose
    private double dLatitude;
    @SerializedName("track_distance")
    @Expose
    private double trackDistance;
    @SerializedName("track_latitude")
    @Expose
    private double trackLatitude;
    @SerializedName("track_longitude")
    @Expose
    private double trackLongitude;
    @SerializedName("d_longitude")
    @Expose
    private double dLongitude;
    @SerializedName("assigned_at")
    @Expose
    private String assignedAt;
    @SerializedName("schedule_at")
    @Expose
    private Object scheduleAt;
    @SerializedName("started_at")
    @Expose
    private Object startedAt;
    @SerializedName("finished_at")
    @Expose
    private Object finishedAt;
    @SerializedName("user_rated")
    @Expose
    private int userRated;
    @SerializedName("provider_rated")
    @Expose
    private int providerRated;
    @SerializedName("use_wallet")
    @Expose
    private int useWallet;
    @SerializedName("surge")
    @Expose
    private int surge;
    @SerializedName("route_key")
    @Expose
    private String routeKey;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("category")
    @Expose
    private Object category;
    @SerializedName("product_type")
    @Expose
    private Object productType;
    @SerializedName("product_weight")
    @Expose
    private Object productWeight;
    @SerializedName("attachment1")
    @Expose
    private Object attachment1;
    @SerializedName("attachment2")
    @Expose
    private Object attachment2;
    @SerializedName("attachment3")
    @Expose
    private Object attachment3;
    @SerializedName("instruction")
    @Expose
    private Object instruction;
    @SerializedName("receiver_name")
    @Expose
    private Object receiverName;
    @SerializedName("receiver_phone")
    @Expose
    private Object receiverPhone;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("provider")
    @Expose
    private Provider provider;
    @SerializedName("service_type")
    @Expose
    private ServiceType serviceType;
    @SerializedName("provider_service")
    @Expose
    private ProviderService providerService;
    @SerializedName("rating")
    @Expose
    private Object rating;
    @SerializedName("payment")
    @Expose
    private Payment payment;

    @SerializedName("provider_longitude")
    @Expose
    private Double provider_longitude;

    @SerializedName("provider_latitude")
    @Expose
    private Double provider_latitude;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public int getCurrentProviderId() {
        return currentProviderId;
    }

    public void setCurrentProviderId(int currentProviderId) {
        this.currentProviderId = currentProviderId;
    }

    public int getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(int serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(String cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public Object getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(Object cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }

    public String getIsTrack() {
        return isTrack;
    }

    public void setIsTrack(String isTrack) {
        this.isTrack = isTrack;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Object getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(Object travelTime) {
        this.travelTime = travelTime;
    }

    public String getSAddress() {
        return sAddress;
    }

    public void setSAddress(String sAddress) {
        this.sAddress = sAddress;
    }

    public double getSLatitude() {
        return sLatitude;
    }

    public void setSLatitude(double sLatitude) {
        this.sLatitude = sLatitude;
    }

    public double getSLongitude() {
        return sLongitude;
    }

    public void setSLongitude(double sLongitude) {
        this.sLongitude = sLongitude;
    }

    public String getDAddress() {
        return dAddress;
    }

    public void setDAddress(String dAddress) {
        this.dAddress = dAddress;
    }

    public double getDLatitude() {
        return dLatitude;
    }

    public void setDLatitude(double dLatitude) {
        this.dLatitude = dLatitude;
    }

    public double getTrackDistance() {
        return trackDistance;
    }

    public void setTrackDistance(int trackDistance) {
        this.trackDistance = trackDistance;
    }

    public double getTrackLatitude() {
        return trackLatitude;
    }

    public void setTrackLatitude(int trackLatitude) {
        this.trackLatitude = trackLatitude;
    }

    public double getTrackLongitude() {
        return trackLongitude;
    }

    public void setTrackLongitude(int trackLongitude) {
        this.trackLongitude = trackLongitude;
    }

    public double getDLongitude() {
        return dLongitude;
    }

    public void setDLongitude(double dLongitude) {
        this.dLongitude = dLongitude;
    }

    public String getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(String assignedAt) {
        this.assignedAt = assignedAt;
    }

    public Object getScheduleAt() {
        return scheduleAt;
    }

    public void setScheduleAt(Object scheduleAt) {
        this.scheduleAt = scheduleAt;
    }

    public Object getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Object startedAt) {
        this.startedAt = startedAt;
    }

    public Object getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Object finishedAt) {
        this.finishedAt = finishedAt;
    }

    public int getUserRated() {
        return userRated;
    }

    public void setUserRated(int userRated) {
        this.userRated = userRated;
    }

    public int getProviderRated() {
        return providerRated;
    }

    public void setProviderRated(int providerRated) {
        this.providerRated = providerRated;
    }

    public int getUseWallet() {
        return useWallet;
    }

    public void setUseWallet(int useWallet) {
        this.useWallet = useWallet;
    }

    public int getSurge() {
        return surge;
    }

    public void setSurge(int surge) {
        this.surge = surge;
    }

    public String getRouteKey() {
        return routeKey;
    }

    public void setRouteKey(String routeKey) {
        this.routeKey = routeKey;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Object getCategory() {
        return category;
    }

    public void setCategory(Object category) {
        this.category = category;
    }

    public Object getProductType() {
        return productType;
    }

    public void setProductType(Object productType) {
        this.productType = productType;
    }

    public Object getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(Object productWeight) {
        this.productWeight = productWeight;
    }

    public Object getAttachment1() {
        return attachment1;
    }

    public void setAttachment1(Object attachment1) {
        this.attachment1 = attachment1;
    }

    public Object getAttachment2() {
        return attachment2;
    }

    public void setAttachment2(Object attachment2) {
        this.attachment2 = attachment2;
    }

    public Object getAttachment3() {
        return attachment3;
    }

    public void setAttachment3(Object attachment3) {
        this.attachment3 = attachment3;
    }

    public Object getInstruction() {
        return instruction;
    }

    public void setInstruction(Object instruction) {
        this.instruction = instruction;
    }

    public Object getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(Object receiverName) {
        this.receiverName = receiverName;
    }

    public Object getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(Object receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public ProviderService getProviderService() {
        return providerService;
    }

    public void setProviderService(ProviderService providerService) {
        this.providerService = providerService;
    }

    public Object getRating() {
        return rating;
    }

    public void setRating(Object rating) {
        this.rating = rating;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Double getProvider_longitude() {
        return provider_longitude;
    }

    public void setProvider_longitude(Double provider_longitude) {
        this.provider_longitude = provider_longitude;
    }

    public Double getProvider_latitude() {
        return provider_latitude;
    }

    public void setProvider_latitude(Double provider_latitude) {
        this.provider_latitude = provider_latitude;
    }
}
