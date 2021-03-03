<#import "template.ftl" as layout>
<@layout.registrationLayout; section>
    <#if section = "title" || section = "header">
        ${msg("sms-auth.title")}
    <#elseif section = "form">
        <#--
            Hack-alert: Keycloak doesn't provide per-field error messages here,
            so we check global message for need to display validation error styling
        -->
        <#if message?has_content && message.type = "error">
            <#assign errorClass = "govuk-form-group--error" >
        </#if>
        <div class="govuk-grid-row">
            <div class="govuk-grid-column-full"><p>${msg("sms-auth.instruction")}</p></div>
            <form id="kc-totp-login-form" class="${properties.kcFormClass!} govuk-grid-column-two-thirds" action="${url.loginAction}" method="post">
                <div class="govuk-form-group  ${errorClass!""} ">
                    <label for="totp" class="govuk-label">${msg("sms-auth.code")}</label>
                    <input id="totp" name="smsCode" type="number" class="govuk-input govuk-input--width-5" autocomplete="false"/>
                </div>
                <input class="govuk-button" name="login" id="kc-login" type="submit" value="${msg("doSubmit")}"/>
            </form>
        </div>
        <#if client?? && client.baseUrl?has_content>
            <p><a id="backToApplication" href="${client.baseUrl}">${msg("backToApplication")}</a></p>
        </#if>
    </#if>
</@layout.registrationLayout>
