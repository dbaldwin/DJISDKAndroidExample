package com.unmannedairlines.djisdkandroidexample;

import android.location.Location;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dji.common.error.DJIError;
import dji.common.gimbal.Attitude;
import dji.common.gimbal.Rotation;
import dji.common.mission.hotpoint.HotpointHeading;
import dji.common.mission.hotpoint.HotpointMission;
import dji.common.mission.hotpoint.HotpointStartPoint;
import dji.common.mission.waypoint.Waypoint;
import dji.common.model.LocationCoordinate2D;
import dji.common.util.CommonCallbacks;
import dji.sdk.flightcontroller.FlightController;
import dji.sdk.mission.MissionControl;
import dji.sdk.mission.timeline.TimelineElement;
import dji.sdk.mission.timeline.TimelineEvent;
import dji.sdk.mission.timeline.TimelineMission;
import dji.sdk.mission.timeline.actions.AircraftYawAction;
import dji.sdk.mission.timeline.actions.GimbalAttitudeAction;
import dji.sdk.mission.timeline.actions.GoHomeAction;
import dji.sdk.mission.timeline.actions.GoToAction;
import dji.sdk.mission.timeline.actions.HotpointAction;
import dji.sdk.mission.timeline.actions.RecordVideoAction;
import dji.sdk.mission.timeline.actions.ShootPhotoAction;
import dji.sdk.mission.timeline.actions.TakeOffAction;
import dji.sdk.products.Aircraft;

import static dji.midware.data.manager.P3.ServiceManager.getContext;

/**
 * Created by db on 5/14/18.
 */

public class MissionLogic {

    public static MissionControl missionControl;
    public static FlightController flightController;
    private TimelineEvent preEvent;
    private TimelineElement preElement;
    private DJIError preError;


    protected double homeLatitude = 181;
    protected double homeLongitude = 181;

    public MissionLogic() {

    }


    public void getHomeLocationAndLaunchMission() {
        if (MApplication.getProductInstance() instanceof Aircraft && !GeneralUtils.checkGpsCoordinate(
                homeLatitude,
                homeLongitude) && flightController != null) {
            flightController.getHomeLocation(new CommonCallbacks.CompletionCallbackWith<LocationCoordinate2D>() {
                @Override
                public void onSuccess(LocationCoordinate2D locationCoordinate2D) {
                    homeLatitude = locationCoordinate2D.getLatitude();
                    homeLongitude = locationCoordinate2D.getLongitude();

                    initTimeline();
                }

                @Override
                public void onFailure(DJIError djiError) {
                    ToastUtils.setResultToToast("Failed to get home coordinates: " + djiError.getDescription());
                }
            });
        }
    }

    public void initTimeline() {
        if (!GeneralUtils.checkGpsCoordinate(homeLatitude, homeLongitude)) {
            ToastUtils.setResultToToast("No home point!!!");
            return;
        }

        ToastUtils.setResultToToast("We are here...");

        List<TimelineElement> elements = new ArrayList<>();

        missionControl = MissionControl.getInstance();
        final TimelineEvent preEvent = null;
        MissionControl.Listener listener = new MissionControl.Listener() {
            @Override
            public void onEvent(@Nullable TimelineElement element, TimelineEvent event, DJIError error) {
                updateTimelineStatus(element, event, error);

            }
        };

        //Step 1: takeoff from the ground
        //setTimelinePlanToText("Step 1: takeoff from the ground");
        elements.add(new TakeOffAction());


        //Step 2: reset the gimbal to horizontal angle in 2 seconds.
        //setTimelinePlanToText("Step 2: set the gimbal pitch -30 angle in 2 seconds");
        //Attitude attitude = new Attitude(-30, Rotation.NO_ROTATION, Rotation.NO_ROTATION);
        //GimbalAttitudeAction gimbalAction = new GimbalAttitudeAction(attitude);
        //gimbalAction.setCompletionTime(2);
        //elements.add(gimbalAction);

        //Step 3: Go 10 meters from home point
        //setTimelinePlanToText("Step 3: Go 10 meters from home point");
        //elements.add(new GoToAction(new LocationCoordinate2D(homeLatitude, homeLongitude), 10));
        elements.add(new GoToAction(10));


        double newLat = homeLatitude + 30 * GeneralUtils.ONE_METER_OFFSET;
        LocationCoordinate2D newLocation = new LocationCoordinate2D(newLat, homeLongitude);
        elements.add(new GoToAction(newLocation, 10));

        elements.add(new AircraftYawAction(180, 20));

        newLat = homeLatitude - 30 * GeneralUtils.ONE_METER_OFFSET;
        newLocation = new LocationCoordinate2D(newLat, homeLongitude);
        elements.add(new GoToAction(newLocation, 10));

        //Step 4: shoot 3 photos with 2 seconds interval between each
        //setTimelinePlanToText("Step 4: shoot 3 photos with 2 seconds interval between each");
        //elements.add(new ShootPhotoAction(3, 2));

        //Step 5: shoot a single photo
        //setTimelinePlanToText("Step 5: shoot a single photo");
        //elements.add(new ShootPhotoAction());

        //Step 6: start recording video
        //setTimelinePlanToText("Step 6: start recording video");
        //elements.add(new RecordVideoAction(true));


        //Step 7: start a waypoint mission while the aircraft is still recording the video
        //setTimelinePlanToText("Step 7: start a waypoint mission while the aircraft is still recording the video");
        //TimelineElement waypointMission = TimelineMission.elementFromWaypointMission(initTestingWaypointMission());
        //elements.add(waypointMission);
        //addWaypointReachedTrigger(waypointMission);

        //Step 8: stop the recording when the waypoint mission is finished
        //setTimelinePlanToText("Step 8: stop the recording when the waypoint mission is finished");
        //elements.add(new RecordVideoAction(false));

        //Step 9: shoot a single photo
        //setTimelinePlanToText("Step 9: shoot a single photo");
        //elements.add(new ShootPhotoAction());

        //Step 10: start a hotpoint mission
        //setTimelinePlanToText("Step 10: start a hotpoint mission to surround 360 degree");
        //HotpointMission hotpointMission = new HotpointMission();
        //hotpointMission.setHotpoint(new LocationCoordinate2D(homeLatitude, homeLongitude));
        //hotpointMission.setAltitude(10);
        //hotpointMission.setRadius(10);
        //hotpointMission.setAngularVelocity(10);
        //HotpointStartPoint startPoint = HotpointStartPoint.NEAREST;
        //hotpointMission.setStartPoint(startPoint);
        //HotpointHeading heading = HotpointHeading.TOWARDS_HOT_POINT;
        //hotpointMission.setHeading(heading);
        //elements.add(new HotpointAction(hotpointMission, 360));

        //Step 11: go back home
        //setTimelinePlanToText("Step 11: go back home");
        //elements.add(new GoHomeAction());

        //Step 12: restore gimbal attitude
        //This last action will delay the timeline to finish after land on ground, which will
        //make sure the AircraftLandedTrigger will be triggered.
        //setTimelinePlanToText("Step 2: set the gimbal pitch -30 angle in 2 seconds");
        //attitude = new Attitude(0, Rotation.NO_ROTATION, Rotation.NO_ROTATION);
        //gimbalAction = new GimbalAttitudeAction(attitude);
        //gimbalAction.setCompletionTime(2);
        //elements.add(gimbalAction);

        //addAircraftLandedTrigger(missionControl);
        //addBatteryPowerLevelTrigger(missionControl);

        if (missionControl.scheduledCount() > 0) {
            missionControl.unscheduleEverything();
            missionControl.removeAllListeners();
        }

        missionControl.scheduleElements(elements);
        missionControl.addListener(listener);

        // Delay a couple seconds and then start the mission
        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                ToastUtils.setResultToToast("Starting timeline...");
                MissionControl.getInstance().startTimeline();
            }
        };
        handler.postDelayed(r, 2000);

    }

    /*private void setTimelinePlanToText(final String s) {

        timelineInfoTV.post(new Runnable() {
            @Override
            public void run() {
                if (timelineInfoTV == null) {
                    Toast.makeText(getContext(), "textview = null", Toast.LENGTH_SHORT).show();
                } else {
                    timelineInfoTV.append(s + "\n");
                }
            }
        });
    }

    private void setRunningResultToText(final String s) {
        runningInfoTV.post(new Runnable() {
            @Override
            public void run() {
                if (runningInfoTV == null) {
                    Toast.makeText(getContext(), "textview = null", Toast.LENGTH_SHORT).show();
                } else {
                    runningInfoTV.append(s + "\n");
                }
            }
        });
    }*/

    private void updateTimelineStatus(@Nullable TimelineElement element, TimelineEvent event, DJIError error) {

        if (element == preElement && event == preEvent && error == preError) {
            return;
        }

        String info = "";

        if (element != null) {
            if (element instanceof TimelineMission) {
                info = ((TimelineMission) element).getMissionObject().getClass().getSimpleName()
                        + " event is "
                        + event.toString()
                        + " "
                        + (error == null ? "" : error.getDescription());
            } else {
                info = element.getClass().getSimpleName()
                        + " event is "
                        + event.toString()
                        + " "
                        + (error == null ? "" : error.getDescription());
            }
        } else {
            info = "Timeline Event is " + event.toString() + " " + (error == null
                    ? ""
                    : "Failed:"
                    + error.getDescription());
        }

        ToastUtils.setResultToToast(info);

        preEvent = event;
        preElement = element;
        preError = error;
    }
}
