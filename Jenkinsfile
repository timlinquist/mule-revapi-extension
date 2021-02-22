def UPSTREAM_PROJECTS_LIST = [ "Mule-runtime/api-annotations/1.0.2-FEBRUARY" ]

Map pipelineParams = [ "upstreamProjects" : UPSTREAM_PROJECTS_LIST.join(','),
                       "mavenSettingsXmlId" : "mule-runtime-maven-settings-MuleSettings",
                       "projectType" : "Runtime",

                       // TODO: Remove this to start using Maven 3.5.x after MULE-15966: The mule-revapi-extension tests fail using Maven version 3.5.x
                       "mavenTool" : "M3" ]

runtimeBuild(pipelineParams)
