#!/usr/bin/env bash

# TODO: Eliminate the need for this script. It's terrible, hacky, and should
# be a gradle task!

(echo "package opticnav.ardd.protocol.consts;"; ./genconst/genconst.py ARDdAdminProtocol.txt ARDdAdminProtocol java -) > ../ardd/ardd-protocol/src/main/java/opticnav/ardd/protocol/consts/ARDdAdminProtocol.java

(echo "package opticnav.ardd.protocol.consts;"; ./genconst/genconst.py ARDdARDProtocol.txt ARDdARDProtocol java -) > ../ardd/ardd-protocol/src/main/java/opticnav/ardd/protocol/consts/ARDdARDProtocol.java

(echo "package opticnav.persistence.web.consts;"; ./genconst/genconst.py WebPersistence.txt WebPersistenceConsts java -) > ../web-persistence/src/main/java/opticnav/persistence/web/consts/WebPersistenceConsts.java

