using LitJson;
using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace WeChartApi.Controllers
{
    public class HomeController : Controller
    {
        //
        // GET: /Home/
        public ActionResult Index()
        {
            string agent = Request.Headers["User-Agent"];
            //判断是否是微信浏览器登录
            //if (util.IsWeiXin(agent))
            //{

                String scCode = Request["sccode"];
                //初始化appid、appsecret
                if (null != scCode && scCode != "")
                {
                    WxInfo wxInfo = getWxInfo(scCode);
                    Session["appid"] = wxInfo.Appid;
                    Session["appsecret"] = wxInfo.Appsecret;
                    Session["url"] = Request["url"];
                }

                String code = Request["code"];
                if (null != code)
                {
                    String url = Session["url"].ToString();
                    String paras = Request["paras"] != null ? Request["paras"] : "";
                    String openid = GetOpenid(code);
                    return Redirect(url + "?openid=" + openid + "&paras=" + paras);
                }
                else
                {
                    return View();
                }
            //}
            //else
            //{
            //    String url = Request["url"];
            //    return Redirect(url);
            //}
        }



        private string GetOpenid(string code)
        {
            string openid = string.Empty;
            try
            {
                //构造获取openid及access_token的url
                WxPayData data = new WxPayData();
                data.SetValue("appid", Session["appid"]);
                data.SetValue("secret", Session["appsecret"]);
                data.SetValue("code", code);
                data.SetValue("grant_type", "authorization_code");
                string url = "https://api.weixin.qq.com/sns/oauth2/access_token?" + data.ToUrl();

                //请求url以获取数据
                string result = HttpService.Get(url);

                Log.Debug(this.GetType().ToString(), "GetOpenidAndAccessTokenFromCode response : " + result);

                //保存access_token，用于收货地址获取
                JsonData jd = JsonMapper.ToObject(result);

                //获取用户openid
                openid = (string)jd["openid"];

                Log.Debug(this.GetType().ToString(), "Get openid : " + openid);

                return openid;
            }
            catch (Exception ex)
            {
                Log.Error(this.GetType().ToString(), ex.ToString());
                //throw new Exception(ex.ToString());
                return "";
            }

        }

        private WxInfo getWxInfo(String scCode)
        {
            string sql = "select * from school_wx where scCode = @scCode";
            DataTable dt = null;
            dt = SqlHelper.ExecuteDataTable(sql, new SqlParameter("@scCode", scCode));
            String appid = dt.Rows[0]["appId"].ToString();
            String appsecret = dt.Rows[0]["appSecret"].ToString();
            return new WxInfo(appid, appsecret);
        }

        public class WxInfo
        {


            private string appid;

            public string Appid
            {
                get { return appid; }
                set { appid = value; }
            }
            private string appsecret;

            public string Appsecret
            {
                get { return appsecret; }
                set { appsecret = value; }
            }

            public WxInfo(string appid, string appsecret)
            {
                this.appid = appid;
                this.appsecret = appsecret;
            }
        }
    }
}