using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;

namespace SmartWardrobe_service
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the interface name "IService1" in both code and config file together.
    [ServiceContract]
    public interface IService1
    {
        [OperationContract]
        [WebGet(UriTemplate = "login/{emailid}/{password}", ResponseFormat = WebMessageFormat.Json)]
        respUserLogin login(String emailid, String password);

        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "register", ResponseFormat = WebMessageFormat.Json)]
        respmsg register(Userdata udata);

        [OperationContract]
        [WebGet(UriTemplate = "usedcloth/{emailid}", ResponseFormat = WebMessageFormat.Json)]
        List<usedlist> usedcloth(String emailid);

        [OperationContract]
        [WebGet(UriTemplate = "unusedcloth/{emailid}", ResponseFormat = WebMessageFormat.Json)]
        List<unusedlist> unusedcloth(String emailid);

        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "uploaddata", ResponseFormat = WebMessageFormat.Json)]
        respmsg uploadData(UploadData uploaddata);
    }

    // Use a data contract as illustrated in the sample below to add composite types to service operations.
    [DataContract]
    public class respUserLogin
    {
        [DataMember]
        public string userid { get; set; }
        [DataMember]
        public String emailid { get; set; }
        [DataMember]
        public String Password { get; set; }
        [DataMember]
        public String Msg { get; set; }
    }

   [DataContract]
   public class respmsg
   {
       [DataMember]
       public string Msg { get; set; }
   }

   [DataContract]
    public class Userdata
    {
       [DataMember]
       public string name { get; set; }
       [DataMember]
       public string age { get; set; }
       [DataMember]
       public string gender { get; set; }
       [DataMember]
       public string emailid { get; set; }
       [DataMember]
       public string password { get; set; }  
    }

   [DataContract]
   public class usedlist
   {
       [DataMember]
       public string cloth_id { get; set; }
       [DataMember]
       public string type { get; set; }

   }

   [DataContract]
   public class unusedlist
   {
       [DataMember]
       public string cloth_id { get; set; }
       [DataMember]
       public string type { get; set; }
   }

   [DataContract]
   public class UploadData
   {
       [DataMember]
       public string type { get; set; }
       [DataMember]
       public string weather { get; set; }
       [DataMember]
       public string occasion { get; set; }
       [DataMember]
       public string rfid { get; set; }
       [DataMember]
       public string color { get; set; }
       [DataMember]
       public string picpath { get; set; }
       [DataMember]
       public string image { get; set; }
   }

   [DataContract]
   public class Imagedata
   {

       [DataMember]
       public string name { get; set; }
       [DataMember]
       public string image { get; set; }
   }

   [DataContract]
   public class ViewImage
   {
       [DataMember]
       public string imageid { get; set; }
       [DataMember]
       public string imagepath { get; set; }
       [DataMember]
       public string imagename { get; set; }
   }
}
