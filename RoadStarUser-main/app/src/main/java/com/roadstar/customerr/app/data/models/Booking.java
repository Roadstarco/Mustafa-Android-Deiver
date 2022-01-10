
package com.roadstar.customerr.app.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.roadstar.customerr.app.business.BaseItem;

public class Booking implements BaseItem {

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
    private String travelTime;

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
    private int trackDistance;
    @SerializedName("track_latitude")
    @Expose
    private int trackLatitude;
    @SerializedName("track_longitude")
    @Expose
    private int trackLongitude;

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
    private String startedAt;
    @SerializedName("finished_at")
    @Expose
    private String finishedAt;
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
    private String category;
    @SerializedName("product_type")
    @Expose
    private String productType;
    @SerializedName("product_weight")
    @Expose
    private String productWeight;
    @SerializedName("attachment1")
    @Expose
    private String attachment1;
    @SerializedName("attachment2")
    @Expose
    private Object attachment2;
    @SerializedName("attachment3")
    @Expose
    private Object attachment3;
    @SerializedName("instruction")
    @Expose
    private String instruction;
    @SerializedName("receiver_name")
    @Expose
    private String receiverName;
    @SerializedName("receiver_phone")
    @Expose
    private String receiverPhone;
    @SerializedName("static_map")
    @Expose
    private String staticMap;
    @SerializedName("payment")
    @Expose
    private Payment payment;
    @SerializedName("service_type")
    @Expose
    private ServiceType serviceType;

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

    public String getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(String travelTime) {
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

    public int getTrackDistance() {
        return trackDistance;
    }

    public void setTrackDistance(int trackDistance) {
        this.trackDistance = trackDistance;
    }

    public int getTrackLatitude() {
        return trackLatitude;
    }

    public void setTrackLatitude(int trackLatitude) {
        this.trackLatitude = trackLatitude;
    }

    public int getTrackLongitude() {
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

    public String getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public String getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(String finishedAt) {
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(String productWeight) {
        this.productWeight = productWeight;
    }

    public String getAttachment1() {
        return attachment1;
    }

    public void setAttachment1(String attachment1) {
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

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getStaticMap() {
        return staticMap;
    }

    public void setStaticMap(String staticMap) {
        this.staticMap = staticMap;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    @Override
    public int getItemType() {
        return 0;
    }
}
