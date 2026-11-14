package odefood;

import java.util.EventListener;

public interface ApplicationStateListener extends EventListener {
	void OnApplicationStateChanged(ApplicationStateEvent e);
}
