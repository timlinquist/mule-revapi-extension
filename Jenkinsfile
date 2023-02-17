def UPSTREAM_PROJECTS_LIST = [ "Mule-runtime/api-annotations/1.2.0-MARCH-2023" ]

Map pipelineParams = [ "upstreamProjects" : UPSTREAM_PROJECTS_LIST.join(','),
                       "mavenSettingsXmlId" : "mule-runtime-maven-settings-MuleSettings",
                       "projectType" : "Runtime"]

runtimeBuild(pipelineParams)
