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
import WatchConnectivity


class LoginViewController: UIViewController,WCSessionDelegate {
    


    var challengeHandler : ChallengeHandler?

    @IBOutlet weak var errorMsg: UILabel!
    @IBOutlet weak var username: UITextField!
    @IBOutlet weak var password: UITextField!
    
    
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        
        commonInit()
    }
    
    
    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: NSBundle?) {
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
        
        commonInit()
    }
    
    func commonInit() {
        
        // Initialize the `WCSession` and the `CLLocationManager`.
        WCSession.defaultSession().delegate = self
        WCSession.defaultSession().activateSession()
    }

    
    override func viewDidLoad() {
        super.viewDidLoad()
        commonInit()
        let dispatchTime = dispatch_time(DISPATCH_TIME_NOW, Int64(1.0 * Double(NSEC_PER_SEC)))
        self.title = "Custom Authentication"
        if (WCSession.defaultSession().paired){
            self.username.text = NSUserDefaults.standardUserDefaults().stringForKey("username1")
            self.password.text = NSUserDefaults.standardUserDefaults().stringForKey("password1")
            dispatch_after(dispatchTime, dispatch_get_main_queue(), {
                self.challengeHandler?.submitLoginForm("/my_custom_auth_request_url",
                    requestParameters: ["username" : self.username.text!, "password" : self.password.text!],
                    requestHeaders: nil, requestTimeoutInMilliSeconds: 0, requestMethod: "POST")
            })
        }
        else
        {
           
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    @IBAction func login(sender: AnyObject) {
        let url = "/my_custom_auth_request_url"
       
        
        NSUserDefaults.standardUserDefaults().setObject(self.username.text, forKey: "username")
        NSUserDefaults.standardUserDefaults().setObject(self.password.text, forKey: "password")
        self.challengeHandler?.submitLoginForm(url,
            requestParameters: ["username" : self.username.text!, "password" : self.password.text!],
            requestHeaders: nil, requestTimeoutInMilliSeconds: 0, requestMethod: "POST")
    }
    
    
    override func viewWillDisappear(animated: Bool) {
        super.viewWillDisappear(true)
        if self.isMovingFromParentViewController(){
            //Back button pressed
            self.challengeHandler?.submitFailure(nil)
        }
    }

}
