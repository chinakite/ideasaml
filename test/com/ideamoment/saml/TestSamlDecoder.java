/**
 * 
 */
package com.ideamoment.saml;

import org.junit.Test;


/**
 * @author Chinakite
 *
 */
public class TestSamlDecoder {
    @Test
    public void testDecode() {
        String str = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+PHNhbWwycDpBdXRoblJlcXVlc3QgeG1sbnM6c2FtbDJwPSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6cHJvdG9jb2wiIEFzc2VydGlvbkNvbnN1bWVyU2VydmljZVVSTD0iaHR0cDovL2xvY2FsaG9zdDo5MDgwL2lkZWFzYW1sLXNwL3NzbyIgSUQ9Il81OWY0MTFiNjNjZDJjYTg5MzliZGI2MTExMzc1Yzg0NSIgSXNzdWVJbnN0YW50PSIyMDE1LTA1LTIxVDE0OjU3OjM4LjAyNVoiIFByb3RvY29sQmluZGluZz0idXJuOm9hc2lzOm5hbWVzOnRjOlNBTUw6Mi4wOmJpbmRpbmdzOkhUVFAtUE9TVCIgUHJvdmlkZXJOYW1lPSJpZGVhc2FtbC1zcCIgVmVyc2lvbj0iMi4wIj48c2FtbDI6SXNzdWVyIHhtbG5zOnNhbWwyPSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6YXNzZXJ0aW9uIj5odHRwOi8vbG9jYWxob3N0OjkwODAvaWRlYXNhbWwtc3A8L3NhbWwyOklzc3Vlcj48c2FtbDJwOk5hbWVJRFBvbGljeS8+PC9zYW1sMnA6QXV0aG5SZXF1ZXN0Pg==";
        
        SamlDecoder d = new SamlDecoder(str);
        
        System.out.println(d.decode());
    }
}
