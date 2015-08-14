/*
*
COPYRIGHT LICENSE: This information contains sample code provided in source code form. You may copy, modify, and distribute
these sample programs in any form without payment to IBM® for the purposes of developing, using, marketing or distributing
application programs conforming to the application programming interface for the operating platform for which the sample code is written.
Notwithstanding anything to the contrary, IBM PROVIDES THE SAMPLE SOURCE CODE ON AN "AS IS" BASIS AND IBM DISCLAIMS ALL WARRANTIES,
EXPRESS OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, ANY IMPLIED WARRANTIES OR CONDITIONS OF MERCHANTABILITY, SATISFACTORY QUALITY,
FITNESS FOR A PARTICULAR PURPOSE, TITLE, AND ANY WARRANTY OR CONDITION OF NON-INFRINGEMENT. IBM SHALL NOT BE LIABLE FOR ANY DIRECT,
INDIRECT, INCIDENTAL, SPECIAL OR CONSEQUENTIAL DAMAGES ARISING OUT OF THE USE OR OPERATION OF THE SAMPLE SOURCE CODE.
IBM HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS OR MODIFICATIONS TO THE SAMPLE SOURCE CODE.

*/

import UIKit


class ViewController: UIViewController {
    
    override func viewDidLoad() {
        super.viewDidLoad()

        NSLog("Connecting to MobileFirst Server...")
        let connectListener = MyConnectListener()
        WLClient.sharedInstance().registerChallengeHandler(MyChallengeHandler(vc: self))
        WLClient.sharedInstance().wlConnectWithDelegate(connectListener)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    @IBAction func callProtectedAdapterProcedure(sender: AnyObject) {
        let url : NSURL = NSURL(string: "/adapters/DigitalBankAdapter/getBalance")!
        let request : WLResourceRequest = WLResourceRequest(URL: url, method: WLHttpMethodGet)
        request.sendWithCompletionHandler { (response: WLResponse!, error: NSError!) -> Void in
            if error != nil {
                NSLog("Adapter invocation failure. Error: %@", error)
                UIAlertView(title: "DigitalBank", message: "Could not get the balance", delegate: nil, cancelButtonTitle: "OK").show()
            } else {
                let adapterResponseAlert = UIAlertController(title: "Adapter Response",
                    message: response.responseText,
                    preferredStyle: .Alert)
                adapterResponseAlert.addAction(UIAlertAction(title: "OK",
                    style: .Default,
                    handler: nil))
                self.presentViewController(adapterResponseAlert,
                    animated: true,
                    completion: nil)
            }

        }
        
    }

    @IBAction func logout(sender: AnyObject) {
        WLClient.sharedInstance().logout("CustomAuthenticatorRealm", withDelegate: MyLogoutListener(vc: self))
    }


}

