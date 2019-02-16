using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Data.SqlClient;
using System.IO;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;
using System.Web;

namespace SmartWardrobe_service
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "Service1" in code, svc and config file together.
    // NOTE: In order to launch WCF Test Client for testing this service, please select Service1.svc or Service1.svc.cs at the Solution Explorer and start debugging.
    public class Service1 : IService1
    {
        String constr = ConfigurationManager.ConnectionStrings["connect"].ConnectionString;
        SqlConnection conn;
        SqlCommand cmd;
        SqlDataAdapter sda;
        SqlDataReader dr;
        String filePath = "";

        public respUserLogin login(String emailid, String password)
        {
            string select = "select * from user_master where Email=@emailid and Password=@password";
            conn = new SqlConnection(constr);
            cmd = new SqlCommand(select, conn);
            cmd.Parameters.AddWithValue("@emailid", emailid);
            cmd.Parameters.AddWithValue("@password", password);
            sda = new SqlDataAdapter(cmd);
            DataTable dt = new DataTable();
            sda.Fill(dt);
            if (dt.Rows.Count > 0)
            {
                return new respUserLogin
                {
                    userid = dt.Rows[0]["Id"].ToString(),
                    emailid = dt.Rows[0]["Email"].ToString(),
                    Password = dt.Rows[0]["Password"].ToString(),
                    Msg = "valid",
                };
            }
            else
            {
                return new respUserLogin
                {
                    Msg = "Invalid"
                };
            }
        }

        public respmsg register(Userdata udata)
        {
            try
            {
                conn = new SqlConnection(constr);
                conn.Open();

                cmd = new SqlCommand("select * from user_master where Email=@emailid", conn);
                cmd.Parameters.AddWithValue("@emailid", udata.emailid);
                SqlDataAdapter sda1 = new SqlDataAdapter(cmd);
                DataTable dt1 = new DataTable();
                sda1.Fill(dt1);
                if (dt1.Rows.Count > 0)
                {
                    return new respmsg
                    {
                        Msg = "Emailid and contact already exist"
                    };
                }
                else
                {
                    cmd = new SqlCommand("insert into user_master(Name,Age,Gender,Email,Password) values (@name,@age,@gender,@emailid,@password)", conn);
                    cmd.Parameters.AddWithValue("@name", udata.name);
                    cmd.Parameters.AddWithValue("@emailid", udata.emailid);
                    cmd.Parameters.AddWithValue("@password", udata.password);
                    cmd.Parameters.AddWithValue("@age", udata.age);
                    cmd.Parameters.AddWithValue("@gender", udata.gender);
                    cmd.ExecuteNonQuery();
                    conn.Close();
                    return new respmsg
                    {
                        Msg = "Data inserted"
                    };
                }
            }
            catch (Exception e)
            {
                return new respmsg
                {
                    Msg = e.ToString()
                };
            }
        }

        public List<usedlist> usedcloth(String emailid)
        {
            conn = new SqlConnection(constr);

            DataTable dt = new DataTable();

            List<usedlist> usedlist = new List<usedlist>();
            try
            {
                using (SqlCommand cmd = new SqlCommand("select * from usage where Email=@email AND Status='used'", conn))
                {
                    cmd.Parameters.AddWithValue("@email", emailid);
                    sda = new SqlDataAdapter(cmd);

                    sda.Fill(dt);
                    if (dt.Rows.Count > 0)
                    {
                        for (int i = 0; i < dt.Rows.Count; i++)
                        {
                            usedlist list = new usedlist
                            {
                                cloth_id = dt.Rows[i]["ClothId"].ToString(),
                                type = dt.Rows[i]["Type"].ToString(),
                            };
                            usedlist.Add(list);
                        }
                    }
                    return usedlist;
                }
            }
            catch (Exception e)
            {
                return usedlist;
            }
            finally
            {
                dt.Dispose();
            }
        }

        public List<unusedlist> unusedcloth(String emailid)
        {
            conn = new SqlConnection(constr);

            DataTable dt = new DataTable();

            List<unusedlist> unusedlist = new List<unusedlist>();
            try
            {
                using (SqlCommand cmd = new SqlCommand("select * from usage where Email=@email AND Status='unused'", conn))
                {
                    cmd.Parameters.AddWithValue("@email", emailid);
                    sda = new SqlDataAdapter(cmd);

                    sda.Fill(dt);
                    if (dt.Rows.Count > 0)
                    {
                        for (int i = 0; i < dt.Rows.Count; i++)
                        {
                            unusedlist list = new unusedlist
                            {
                                cloth_id = dt.Rows[i]["ClothId"].ToString(),
                                type = dt.Rows[i]["Type"].ToString(),
                            };
                            unusedlist.Add(list);
                        }
                    }
                    return unusedlist;
                }
            }
            catch (Exception e)
            {
                return unusedlist;
            }
            finally
            {
                dt.Dispose();
            }
        }

        public respmsg uploadData(UploadData upload)
        {
            try
            {
                conn = new SqlConnection(constr);
                conn.Open();
                string MatchPath = "";
                byte[] imagebytes = Convert.FromBase64String(upload.image);
                filePath = HttpContext.Current.Server.MapPath("~/Images/" + Path.GetFileName(upload.rfid) + ".jpg");
                string path = "Images/" + Path.GetFileName(upload.rfid) + ".jpg";
                File.WriteAllBytes(filePath, imagebytes);

                cmd = new SqlCommand("select * from clothes_master where Picpath=@picpath", conn);
                cmd.Parameters.AddWithValue("@picpath", path);

                SqlDataAdapter daimg = new SqlDataAdapter(cmd);
                DataTable dtimg = new DataTable();
                daimg.Fill(dtimg);
                if (dtimg.Rows.Count > 0)
                {
                    MatchPath = dtimg.Rows[0]["Picpath"].ToString();
                }
                if (path.Equals(MatchPath))
                {
                    return new respmsg
                    {
                        Msg = "Image already exist"
                    };
                }
                else
                {
                    SqlCommand cmd1 = new SqlCommand("Insert into clothes_master(Descp,Weather,Occasion,Color,Rfid,Picpath) values (@descp,@weather,@occasion,@color,@rfid,@picpath)", conn);
                    cmd1.Parameters.AddWithValue("@descp", upload.type);
                    cmd1.Parameters.AddWithValue("@weather", upload.weather);
                    cmd1.Parameters.AddWithValue("@occasion", upload.occasion);
                    cmd1.Parameters.AddWithValue("@color", upload.color);
                    cmd1.Parameters.AddWithValue("@rfid", upload.rfid);
                    cmd1.Parameters.AddWithValue("@picpath", path);
                    cmd1.ExecuteNonQuery();
                    conn.Close();
                    return new respmsg
                    {
                        Msg = "Data saved"
                    };
                }
            }
            catch (Exception e)
            {
                return new respmsg
                {
                    Msg = e.ToString()
                };
            }
        }
    }
}
