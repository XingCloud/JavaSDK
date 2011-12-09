/**
* SmartFoxServer 2X
* Actionscript 3 Basic Code Template
* 
* (c) 2009 - 2010 gotoAndPlay()
*/
package
{
	import com.smartfoxserver.v2.SmartFox
	import com.smartfoxserver.v2.core.SFSEvent
	import com.smartfoxserver.v2.entities.*
	import com.smartfoxserver.v2.entities.data.*
	import com.smartfoxserver.v2.requests.*
	
	import flash.display.*
	import flash.events.*
	import flash.system.Security
	
	public class SFS2XConnector extends Sprite
	{	
		private var sfs:SmartFox
		
		public function SFS2XConnector()
		{
			// Create an instance of the SmartFox class
		 	sfs = new SmartFox()
		
			// Turn on the debug feature
			sfs.debug = true
			
			// Add SFS2X event listeners
			sfs.addEventListener(SFSEvent.CONNECTION, onConnection)
			sfs.addEventListener(SFSEvent.CONNECTION_LOST, onConnectionLost)
			sfs.addEventListener(SFSEvent.CONFIG_LOAD_SUCCESS, onConfigLoadSuccess)
			sfs.addEventListener(SFSEvent.CONFIG_LOAD_FAILURE, onConfigLoadFailure)
			
			// Connect button listener
			bt_connect.addEventListener(MouseEvent.CLICK, onBtConnectClick)

			dTrace("SmartFox API: "+ sfs.version)
			dTrace("Click the CONNECT button to start...")
		}	
		
		//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
		// Button Handlers
		//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
		private function onBtConnectClick(evt:Event):void
		{
			// Load the default configuration file, config.xml
			sfs.loadConfig()
		}
		
		
		//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
		// SFS2X Event Handlers
		//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
		private function onConnection(evt:SFSEvent):void
		{
			if (evt.params.success)
			{
				dTrace("Connection Success!")
			}
			else
			{
				dTrace("Connection Failure: " + evt.params.errorMessage)
			}
		}
		
		private function onConnectionLost(evt:SFSEvent):void
		{
			dTrace("Connection was lost. Reason: " + evt.params.reason)
		}
		
		private function onConfigLoadSuccess(evt:SFSEvent):void
		{
			dTrace("Config load success!")
			dTrace("Server settings: "  + sfs.config.host + ":" + sfs.config.port)
		}
		
		private function onConfigLoadFailure(evt:SFSEvent):void
		{
			dTrace("Config load failure!!!")
		}

		//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
		// Utility Methods
		//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
		private function dTrace(msg:String):void
		{
			ta_debug.text += "--> " + msg + "\n";
		}
		
	}
}
