// hooks.server.js
export const handle = async({event, resolve}) => {
    const response = await resolve(event, {
      transformPageChunk: ({html}) => {
        // This section will modify the HTML 
        // before being returned to the client
        const cookies = event.cookies;
        const themeCookieName = "x-app-theme";
        let currentTheme = cookies.get(themeCookieName);  
        if(!currentTheme) {
          currentTheme = "default-theme";
          cookies.set(themeCookieName, currentTheme, { 
            secure: false, 
            httpOnly: false,
            path: "/" })
        }
        let result = html.replace(`data-theme=""`, `data-theme="${currentTheme}"`);
        return result;
      }
    });
  
    return response;
  }