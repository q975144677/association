package com.association.config.iface;

import com.association.config.model.AuthDTO;
import com.association.config.model.SchoolDTO;
import component.Proto;

public interface ConfigIface {
    Proto<SchoolDTO> schools();

    Proto<AuthDTO> auth();
}
