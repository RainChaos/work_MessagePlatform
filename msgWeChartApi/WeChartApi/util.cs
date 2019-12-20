using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WeChartApi
{
    public class util
    {
        //判断是否为微信浏览器  
        public static bool IsWeiXin(string userAgent)
        {
            if (userAgent.IndexOf("MicroMessenger") > -1)// Nokia phones and emulators   
            {
                return true;
            }
            else
            {
                return false;
            }
        }  
    }
}