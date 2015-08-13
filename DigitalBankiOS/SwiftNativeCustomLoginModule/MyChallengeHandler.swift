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
import Foundation

class MyChallengeHandler: ChallengeHandler {
    
    let vc : ViewController
    
    init(vc: ViewController){
        self.vc = vc
        super.init(realm: "CustomAuthenticatorRealm")
    }
    
    override func isCustomResponse(response: WLResponse!) -> Bool {
        if response != nil && response.responseJSON != nil {
            let responseJson: NSDictionary = response.responseJSON as NSDictionary
            if responseJson.objectForKey("authStatus") != nil{
                let authRequired : NSString = responseJson.objectForKey("authStatus") as! NSString
                return (authRequired.compare("required") == NSComparisonResult.OrderedSame)
            }
        }
        
        
        return false
    }
    
    override func handleChallenge(response: WLResponse!) {
        NSLog("A login form should appear")
        if self.vc.navigationController?.visibleViewController!.isKindOfClass(LoginViewController) == true {
            dispatch_async(dispatch_get_main_queue()) {
                let loginController : LoginViewController! = self.vc.navigationController?.visibleViewController as? LoginViewController
                loginController.errorMsg.hidden = false
            }
        } else {
            self.vc.performSegueWithIdentifier("showLogin", sender: self.vc)
            dispatch_async(dispatch_get_main_queue()) {
                let loginController : LoginViewController! = self.vc.navigationController?.visibleViewController as? LoginViewController
                loginController.challengeHandler = self
                loginController.errorMsg.hidden = true
            }
            
        }
    }
    
    override func onSuccess(response: WLResponse!) {
        NSLog("Challenge succeeded")
        self.vc.navigationController?.popViewControllerAnimated(true)
        NSUserDefaults.standardUserDefaults().setObject(NSUserDefaults.standardUserDefaults().stringForKey("username"), forKey: "username1")
        NSUserDefaults.standardUserDefaults().setObject(NSUserDefaults.standardUserDefaults().stringForKey("password"), forKey: "password1")
        
       
        self.submitSuccess(response)
    }
   
    override func onFailure(response: WLFailResponse!) {
        NSLog("Challenge failed")
        self.submitFailure(response)
    }
    
}
