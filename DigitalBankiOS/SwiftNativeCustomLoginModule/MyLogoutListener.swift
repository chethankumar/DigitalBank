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

class MyLogoutListener: NSObject, WLDelegate {
    
    var vc : ViewController
    
    init(vc: ViewController){
        self.vc = vc
    }
    
    func onSuccess(response: WLResponse!) {
        NSLog("Logout Succeeded")
        let adapterResponseAlert = UIAlertController(title: "Logout Succeeded",
                                                    message: response.responseText,
                                                    preferredStyle: .Alert)
        adapterResponseAlert.addAction(UIAlertAction(title: "OK",
                                                    style: .Default,
                                                    handler: nil))
        vc.presentViewController(adapterResponseAlert,
                                animated: true,
                                completion: nil)
        
    }
    
    func onFailure(response: WLFailResponse!) {
        NSLog("Logout Failed")
        let adapterResponseAlert = UIAlertController(title: "Logout Failed",
                                                    message: response.errorMsg,
                                                    preferredStyle: .Alert)
        adapterResponseAlert.addAction(UIAlertAction(title: "OK",
                                                    style: .Default,
                                                    handler: nil))
        vc.presentViewController(adapterResponseAlert,
                                animated: true,
                                completion: nil)
    }
}
