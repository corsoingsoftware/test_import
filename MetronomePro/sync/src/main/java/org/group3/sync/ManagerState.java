package org.group3.sync;

import org.group3.sync.exception.ConnectivityException;

/**
 * Created by summer on 21/12/16.
 */

interface ManagerState {
	void onActivityResume();
	void onActivityPaused();
	void terminate();
}
